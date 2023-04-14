package com.example.mymoneytracker;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyMoneyTrackerApp extends Application {
    private static final String DB_NAME = "db_MyMoneyTracker";
    private static final int DB_VERSION = 1;

    private SQLiteOpenHelper helper;

    @Override
    public void onCreate() {
        super.onCreate();
        helper = new SQLiteOpenHelper(this, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS tblUser (" +
                        "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "Name VARCHAR(255) NOT NULL," +
                        "Email VARCHAR(255)," +
                        "Password VARCHAR(255) NOT NULL," +
                        "CreationStamp INT)");

                db.execSQL("CREATE TABLE IF NOT EXISTS tblCategory (" +
                        "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "Type INT NOT NULL," +
                        "Description VARCHAR(255), " +
                        "CreationStamp INT )");

                db.execSQL("CREATE TABLE IF NOT EXISTS tblTransaction (" +
                        "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "UserId INT NOT NULL," +
                        "TransactionDate INT NOT NULL," +
                        "Description VARCHAR(255)," +
                        "CategoryId INT NOT NULL," +
                        "Amount Double(10,2) NOT NULL," +
                        "Photo MEDIUMBLOB," +
                        "CreationStamp INT," +
                        "FOREIGN KEY (UserId) REFERENCES tblUser(Id)," +
                        "FOREIGN KEY (CategoryId) REFERENCES tblCategory(Id))");


            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        };
    }

    public boolean LoginUser(String userName, String password){
        boolean result = false;
        String dbUser = "";
        String dbPassword = "";

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Name, Password FROM tblUser WHERE Name = \"" + userName + "\";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            dbUser = cursor.getString(0);
            dbPassword = cursor.getString(1);

            if (dbPassword.equals(password)){
                result = true;
            }
        }
        cursor.close();
        return result;
    }

    public int GetUserId(String userName){
        int result = -1;

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Id FROM tblUser WHERE name = \"" + userName + "\";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    public boolean AddUser(String userName, String userEmail, String userPassword) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long creationTime = System.currentTimeMillis();
        db.execSQL("INSERT INTO tblUser (Name, Email, Password, CreationStamp)" +
                   "VALUES (\"" + userName + "\", \"" + userEmail + "\", \"" + userPassword + "\", " + creationTime + ");");
        return true;
    }

    public void AddCategory(int type, String catDescription) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long creationTime = System.currentTimeMillis();
        db.execSQL("INSERT INTO tblCategory (type, Description, CreationStamp)" +
                "VALUES (" + type + ", \"" + catDescription + "\", " + creationTime + ");");
    }

    public void AddTransaction(int userId,
                               long transactionDate,
                               String transacDescription,
                               int transacCategory,
                               double transacAmount) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long creationTime = System.currentTimeMillis();
        db.execSQL("INSERT INTO tblTransaction (UserId, TransactionDate, Description, CategoryId, Amount, CreationStamp) " +
                "VALUES (" + userId + ", " + transactionDate + ", \"" + transacDescription + "\", " + transacCategory +
                ", " + transacAmount + ", " + creationTime + ");");
    }

    public String[] GetCategoriesByType(String typeStr) {

        String[] result;
        Cursor cursor;
        SQLiteDatabase db = helper.getReadableDatabase();
        if (typeStr.isEmpty()) {
            cursor = db.rawQuery("SELECT Description FROM tblCategory ORDER BY Type, Description;", null);
        }
        else if (typeStr.equals("I")){ // typestr = "I" ==> INCOME
            cursor = db.rawQuery("SELECT Description FROM tblCategory WHERE Type = 1 " + // 1 ==> INCOME
                                     " ORDER BY Description;", null);
        } else { // typestr = "E" ==> EXPENSE
            cursor = db.rawQuery("SELECT Description FROM tblCategory WHERE Type = 2" + // 2 ==> EXPENSE
                    " ORDER BY Description;", null);
        }
        if (cursor.getCount() > 0) {
            int index = cursor.getCount();
            result = new String[index];
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); ++i){
                result[i] = "" + cursor.getString(0);
                cursor.moveToNext();
            }
        }
        else {
            result = new String[1];
        }
        cursor.close();
        return result;
    }

    public int GetCategoryByName(String categoryName) {
        int result = -1;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Id FROM tblCategory WHERE Description = \"" + categoryName + "\" ;", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }


    public ArrayList<LVItemTransaction> GetLastTenTransactions(int userId) {
        ArrayList<LVItemTransaction> result = new ArrayList<LVItemTransaction>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT t.Description, c.Description, t.Amount, t.TransactionDate, c.Type, t.Id " +
                                        "FROM tblTransaction t JOIN tblCategory c ON t.CategoryId = c.Id " +
                                        "WHERE t.UserId = " + userId + " ORDER BY t.TransactionDate DESC LIMIT 10;", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); ++i){
                String t_Description = cursor.getString(0);
                String t_Category = cursor.getString(1);
                Double t_Amount = cursor.getDouble(2);
                long t_Date = cursor.getLong(3);
                int t_CategoryType = cursor.getInt(4);
                int t_Image = (t_CategoryType == 1 ? R.drawable.cat_leisure : R.drawable.cat_education);
                int t_TransacId = cursor.getInt(5);
                LVItemTransaction transac = new LVItemTransaction(t_Description, t_Category, t_Amount, t_Date, t_Image, t_TransacId, t_CategoryType);
                result.add(transac);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return result;
    }

    public LVItemTransaction GetTransactionbyId(int transactionId) {
        LVItemTransaction result = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT t.Description, c.Description, t.Amount, t.TransactionDate, c.Type, t.Id " +
                "FROM tblTransaction t JOIN tblCategory c ON t.CategoryId = c.Id " +
                "WHERE t.Id = " + transactionId + ";", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            String t_Description = cursor.getString(0);
            String t_Category = cursor.getString(1);
            Double t_Amount = cursor.getDouble(2);
            long t_Date = cursor.getLong(3);
            int t_CategoryType = cursor.getInt(4);
            int t_Image = (t_CategoryType == 1 ? R.drawable.cat_leisure : R.drawable.cat_education);
            int t_TransacId = cursor.getInt(5);
            result = new LVItemTransaction(t_Description, t_Category, t_Amount, t_Date, t_Image, t_TransacId, t_CategoryType);
        }
        cursor.close();

        return result;

    }

    public void DeleteTransactionById(int transactionId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long creationTime = System.currentTimeMillis();
        db.execSQL("DELETE FROM tblTransaction WHERE Id = " + transactionId + ";");
    }

    public void UpdateTransaction(int transactionId,
                               int userId,
                               long transactionDate,
                               String transacDescription,
                               int transacCategory,
                               double transacAmount) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long creationTime = System.currentTimeMillis();
        db.execSQL("UPDATE tblTransaction  SET TransactionDate = " + transactionDate + ", Description = \"" + transacDescription + "\", " +
                        "CategoryId = " + transacCategory + ", Amount = " + transacAmount + ", CreationStamp = " + creationTime +
                        " WHERE Id = " + transactionId + ";");
    }

    public double GetIncomeBalance(int userId) {
        double result;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(t.Amount) FROM tblTransaction t JOIN tblCategory c ON t.CategoryId = c.Id " +
                "WHERE c.Type = 1 AND t.UserId = " + userId + ";", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursor.getDouble(0);
        } else {
            result = 0;
        }
        cursor.close();

        return result;
    }

    public double GetExpenseBalance(int userId) {
        double result;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(t.Amount) FROM tblTransaction t JOIN tblCategory c ON t.CategoryId = c.Id " +
                "WHERE c.Type = 2 AND t.UserId = " + userId + ";", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursor.getDouble(0);
        } else {
            result = 0;
        }
        cursor.close();

        return result;
    }

    public String GetUserName(int userId) {
        String result;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Name FROM tblUser WHERE Id = " + userId + ";", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursor.getString(0);
        } else {
            result = "";
        }
        cursor.close();

        return result;
    }

    public ArrayList<LVItemTransaction> GetAllTransactions(int userId) {
        ArrayList<LVItemTransaction> result = new ArrayList<LVItemTransaction>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT t.Description, c.Description, t.Amount, t.TransactionDate, c.Type, t.Id " +
                "FROM tblTransaction t JOIN tblCategory c ON t.CategoryId = c.Id " +
                "WHERE t.UserId = " + userId + " ORDER BY t.TransactionDate DESC;", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); ++i){
                String t_Description = cursor.getString(0);
                String t_Category = cursor.getString(1);
                Double t_Amount = cursor.getDouble(2);
                long t_Date = cursor.getLong(3);
                int t_CategoryType = cursor.getInt(4);
                int t_Image = (t_CategoryType == 1 ? R.drawable.cat_leisure : R.drawable.cat_education);
                int t_TransacId = cursor.getInt(5);
                LVItemTransaction transac = new LVItemTransaction(t_Description, t_Category, t_Amount, t_Date, t_Image, t_TransacId, t_CategoryType);
                result.add(transac);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return result;
    }
}