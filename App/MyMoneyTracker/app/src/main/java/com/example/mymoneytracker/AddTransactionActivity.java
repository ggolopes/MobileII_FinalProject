package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddTransactionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] categories;
    private int userId;
    private String categoryName;
    private long calendarDate;

    private RadioButton rBtnAddTransacIncome;
    private RadioButton rBtnAddTransacExpense;
    private Spinner spinnerCategory;
    private EditText editTextAddTransacValue;
    private EditText editTextAddTransacDescription;
    private CalendarView calendarViewAddTransacDate;
    private TextView tvAddTransacMessage;
    private Button btnAddTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String user = getIntent().getStringExtra("UserId");
        userId = Integer.valueOf(getIntent().getStringExtra("UserId"));

        rBtnAddTransacIncome = findViewById(R.id.rBtnAddTransacIncome);
        rBtnAddTransacExpense = findViewById(R.id.rBtnAddTransacExpense);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextAddTransacValue = findViewById(R.id.editTextAddTransacValue);
        editTextAddTransacDescription = findViewById(R.id.editTextAddTransacDescription);
        calendarViewAddTransacDate = findViewById(R.id.calendarViewAddTransacDate);
        tvAddTransacMessage = findViewById(R.id.tvAddTransacMessage);
        btnAddTransaction = findViewById(R.id.btnAddTransaction);

        calendarViewAddTransacDate.setDate(System.currentTimeMillis());
        calendarDate = calendarViewAddTransacDate.getDate();
        calendarViewAddTransacDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance(TimeZone.getTimeZone("EST"));
                newDate.set(year, month, dayOfMonth, 8,0,0);
                calendarDate = newDate.getTimeInMillis();
            }
        });
        categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType(""); // ==> All Categories
        spinnerCategory.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories));
        spinnerCategory.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            //go back to your activity / exiting the SettingsActivity
            onBackPressed();
        }
        return true;
    }

    public void ChangeCategoryType(View view) {
        if (rBtnAddTransacIncome.isChecked()){
            categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType("I"); // ==> INCOME
        }
        else {
            categories = ((MyMoneyTrackerApp)getApplication()).GetCategoriesByType("E"); // ==> EXPENSE
        }
        spinnerCategory.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories));
    }


    //"INSERT INTO tblTransaction (userId, transactioDate, Description, CategoryId, Amount, CreationStamp)
    public void DoAddTransaction(View view) {
        if (!rBtnAddTransacIncome.isChecked() && !rBtnAddTransacExpense.isChecked()){
            tvAddTransacMessage.setText("Please inform the transaction type.");
        } else if (editTextAddTransacValue.getText().toString().isEmpty()) {
            tvAddTransacMessage.setText("Please inform a valid transaction value");
        } else if (editTextAddTransacDescription.getText().toString().isEmpty()){
            tvAddTransacMessage.setText("Please inform the transaction description.");
        } else {
            long t_date = calendarViewAddTransacDate.getDate();
            int transacCategory = ((MyMoneyTrackerApp)getApplication()).GetCategoryByName(categoryName);
            ((MyMoneyTrackerApp)getApplication()).AddTransaction(userId,
                    calendarDate,
                    editTextAddTransacDescription.getText().toString(),
                    transacCategory,
                    Double.parseDouble(editTextAddTransacValue.getText().toString())
                    );
            Toast.makeText(this, "The transaction was added to the database.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryName = categories[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        categoryName = "";
    }
}