package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class ViewTransactionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String[] categories;
    private LVItemTransaction transaction;
    private int userId;
    private int transactionId;
    private String categoryName;
    private long calendarDate;

    private RadioButton rBtnViewTransacIncome;
    private RadioButton rBtnViewTransacExpense;
    private Spinner spinnerViewCategory;
    private EditText editTextViewTransacValue;
    private EditText editTextViewTransacDescription;
    private CalendarView calendarViewViewTransacDate;
    private TextView tvViewTransacMessage;
    private Button btnViewDelete;
    private Button btnViewSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String user = getIntent().getStringExtra("UserId");
        String t_id = getIntent().getStringExtra("TransactionId");
        sharedPreferences = getSharedPreferences("LastInput", MODE_PRIVATE);
        userId = Integer.valueOf(getIntent().getStringExtra("UserId"));
        transactionId = Integer.valueOf(getIntent().getStringExtra("TransactionId"));
        transaction = ((MyMoneyTrackerApp)getApplication()).GetTransactionbyId(transactionId);


        rBtnViewTransacIncome = findViewById(R.id.rBtnViewTransacIncome);
        rBtnViewTransacExpense = findViewById(R.id.rBtnViewTransacExpense);
        spinnerViewCategory = findViewById(R.id.spinnerViewCategory);
        editTextViewTransacValue = findViewById(R.id.editTextViewTransacValue);
        editTextViewTransacDescription = findViewById(R.id.editTextViewTransacDescription);
        calendarViewViewTransacDate = findViewById(R.id.calendarViewViewTransacDate);
        tvViewTransacMessage = findViewById(R.id.tvViewTransacMessage);
        btnViewDelete = findViewById(R.id.btnViewDelete);
        btnViewSave = findViewById(R.id.btnViewSave);
        spinnerViewCategory.setOnItemSelectedListener(this);


        if(transaction.getCategoryType() == 1){ // Type = 1 ==> INCOME
            rBtnViewTransacIncome.setChecked(true);
            categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType("I"); // ==> INCOME
            spinnerViewCategory.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories));
        } else { // Type = 2 ==> EXPENSE
            rBtnViewTransacExpense.setChecked(true);
            categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType("E"); // ==> EXPENSE
            spinnerViewCategory.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories));
        }
        spinnerViewCategory.setSelection(((ArrayAdapter<String>)spinnerViewCategory.getAdapter()).getPosition(transaction.getCategory()));
        categoryName = transaction.getCategory();

        editTextViewTransacDescription.setText(transaction.getDescription());
        editTextViewTransacValue.setText("" + transaction.getAmount());
        calendarViewViewTransacDate.setDate(transaction.getDate());
        calendarDate = calendarViewViewTransacDate.getDate();
        calendarViewViewTransacDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance(TimeZone.getTimeZone("EST"));
                    newDate.set(year, month, dayOfMonth, 8,0,0);
                    calendarDate = newDate.getTimeInMillis();
            }
        });

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ViewTransacDate", (""+calendarDate));
        editor.putString("ViewTransacDescription", editTextViewTransacDescription.getText().toString());
        editor.putString("ViewTransacValue", editTextViewTransacValue.getText().toString());
        editor.putString("ViewTransacCategory", categoryName);
        editor.putBoolean("ViewTransacIncome", rBtnViewTransacIncome.isChecked());
        editor.putBoolean("ViewTransacExpense", rBtnViewTransacExpense.isChecked());
        editor.putString("ViewTransacMessage", tvViewTransacMessage.getText().toString());
        editor.commit();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            //go back to your activity / exiting the SettingsActivity
            ClearFieldsViewTransaction();
            onBackPressed();
        }
        return true;
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryName = categories[position];
    }

    public void onNothingSelected(AdapterView<?> parent) {
        categoryName = "";
    }

    public void DoDeleteTransaction(View view) {
        AlertDialog.Builder DeleteConfirmation = new AlertDialog.Builder(this);
        DeleteConfirmation.setTitle("Delete Transaction.");
        DeleteConfirmation.setIcon(R.drawable.delete1);
        DeleteConfirmation.setMessage("Do you really want to delete this transaction?");
        DeleteConfirmation.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MyMoneyTrackerApp)getApplication()).DeleteTransactionById(transactionId);
                Toast.makeText(ViewTransactionActivity.this, "The transaction was deleted.", Toast.LENGTH_SHORT).show();
                ClearFieldsViewTransaction();
                finish();
            }
        });
        DeleteConfirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        DeleteConfirmation.show();

    }

    private void ClearFieldsViewTransaction() {
        calendarViewViewTransacDate.setDate((System.currentTimeMillis()));
        calendarDate = calendarViewViewTransacDate.getDate();
        editTextViewTransacDescription.setText("");
        editTextViewTransacValue.setText("");
        categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType(""); // ==> All Categories
        spinnerViewCategory.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories));
        rBtnViewTransacIncome.setChecked(false);
        rBtnViewTransacExpense.setChecked(false);
        tvViewTransacMessage.setText("");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("ViewTransacDate");
        editor.remove("ViewTransacDescription");
        editor.remove("ViewTransacValue");
        editor.remove("ViewTransacCategory");
        editor.remove("ViewTransacIncome");
        editor.remove("ViewTransacExpense");
        editor.remove("ViewTransacMessage");
        editor.commit();
    }

    public void DoSaveTransaction(View view) {
        if (!rBtnViewTransacIncome.isChecked() && !rBtnViewTransacExpense.isChecked()){
            tvViewTransacMessage.setText("Please inform the transaction type.");
        } else if (editTextViewTransacValue.getText().toString().isEmpty()) {
            tvViewTransacMessage.setText("Please inform a valid transaction value");
        } else if (editTextViewTransacDescription.getText().toString().isEmpty()){
            tvViewTransacMessage.setText("Please inform the transaction description.");
        } else {
            int transacCategory = ((MyMoneyTrackerApp)getApplication()).GetCategoryByName(categoryName);
            ((MyMoneyTrackerApp)getApplication()).UpdateTransaction( transactionId,
                    userId,
                    calendarDate,
                    editTextViewTransacDescription.getText().toString(),
                    transacCategory,
                    Double.parseDouble(editTextViewTransacValue.getText().toString())
            );
            Toast.makeText(this, "The transaction was updated.", Toast.LENGTH_SHORT).show();
            ClearFieldsViewTransaction();
            finish();
        }
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ViewTransacDate", (""+calendarDate));
        editor.putString("ViewTransacDescription", editTextViewTransacDescription.getText().toString());
        editor.putString("ViewTransacValue", editTextViewTransacValue.getText().toString());
        editor.putString("ViewTransacCategory", categoryName);
        editor.putBoolean("ViewTransacIncome", rBtnViewTransacIncome.isChecked());
        editor.putBoolean("ViewTransacExpense", rBtnViewTransacExpense.isChecked());
        editor.putString("ViewTransacMessage", tvViewTransacMessage.getText().toString());
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        calendarDate = Long.parseLong(sharedPreferences.getString("ViewTransacDate","0"));
        editTextViewTransacValue.setText(sharedPreferences.getString("ViewTransacValue", ""));
        categoryName = sharedPreferences.getString("ViewTransacCategory", "");
        rBtnViewTransacIncome.setChecked(sharedPreferences.getBoolean("ViewTransacIncome", false));
        rBtnViewTransacExpense.setChecked(sharedPreferences.getBoolean("ViewTransacExpense", false));

        if(rBtnViewTransacIncome.isChecked()){ // Type = 1 ==> INCOME
            categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType("I"); // ==> INCOME
            spinnerViewCategory.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories));
        } else { // Type = 2 ==> EXPENSE
            categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType("E"); // ==> EXPENSE
            spinnerViewCategory.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories));
        }
        spinnerViewCategory.setSelection(((ArrayAdapter<String>)spinnerViewCategory.getAdapter()).getPosition(categoryName));
        tvViewTransacMessage.setText(sharedPreferences.getString("ViewTransacMessage", ""));
    }
}