package com.example.mymoneytracker;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

//                db.execSQL("CREATE TABLE IF NOT EXISTS tblType (" +
//                        "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
//                        "Name VARCHAR(255) NOT NULL," +
//                        "CreationStamp INT)");

                db.execSQL("CREATE TABLE IF NOT EXISTS tblCategory (" +
                        "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "Type INT NOT NULL," +
                        "Description VARCHAR(255), " +
                        "CreationStamp INT )");

                db.execSQL("CREATE TABLE IF NOT EXISTS tblTransaction (" +
                        "Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "UserId INT NOT NULL," +
                        "TransactionDate DATE NOT NULL," +
                        "Description VARCHAR(255)," +
                        "TypeId INT NOT NULL," +
                        "CategoryId INT NOT NULL," +
                        "Amount Double(10,2) NOT NULL," +
                        "Photo MEDIUMBLOB," +
                        "CreationStamp INT," +
                        "FOREIGN KEY (UserId) REFERENCES tblUser(Id)," +
                        "FOREIGN KEY (TypeId) REFERENCES tblType(Id)," +
                        "FOREIGN KEY (CategoryId) REFERENCES tblCategory(Id))");


            }

//            void DoCheckTypeTable() {
//                SQLiteDatabase db = helper.getReadableDatabase();
//                Cursor cursor = db.rawQuery("SELECT Id, Name FROM tblType",null);
//                if (cursor.getCount() <= 0) {
//                    cursor.close();
//                    SQLiteDatabase dbIns = helper.getWritableDatabase();
//                    int creationTime = (int)(System.currentTimeMillis()/1000);
//                    dbIns.execSQL("INSERT INTO tblType (Name, CreationStamp)" +
//                            "VALUES (\"Income\", " + creationTime + ")," +
//                                   "(\"Expense\", " + creationTime + ");");
//                }
//                else {
//                    cursor.close();
//                }
//            };

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
        int creationTime = (int)(System.currentTimeMillis()/1000);
        db.execSQL("INSERT INTO tblUser (Name, Email, Password, CreationStamp)" +
                   "VALUES (\"" + userName + "\", \"" + userEmail + "\", \"" + userPassword + "\", " + creationTime + ");");
        return true;
    }

    public void AddCategory(int type, String catDescription) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int creationTime = (int)(System.currentTimeMillis()/1000);
        db.execSQL("INSERT INTO tblCategory (type, Description, CreationStamp)" +
                "VALUES (" + type + ", \"" + catDescription + "\", " + creationTime + ");");
    }

    public String[] GetAllCategories() {
        String[] result;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Id FROM tblUser WHERE name = \"" + userName + "\";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;

    }
}
