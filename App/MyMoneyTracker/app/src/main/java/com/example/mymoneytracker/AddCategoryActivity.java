package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText editTextDescription;
    private RadioButton rBtnAddCategoryIncome;
    private RadioButton rBtnAddCategoryExpense;
    private TextView tvAddCategoryMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        editTextDescription = findViewById(R.id.editTextAddCategoryDescription);
        rBtnAddCategoryIncome = findViewById(R.id.rBtnAddCategoryIncome);
        rBtnAddCategoryExpense = findViewById(R.id.rBtnAddCategoryExpense);
        tvAddCategoryMessage = findViewById(R.id.tvAddCategoryMessage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            //go back to your activity / exiting the SettingsActivity
            onBackPressed();
        }
        return true;
    }

    public void DoAddCategory(View view) {
        tvAddCategoryMessage.setText("");
        if (!rBtnAddCategoryIncome.isChecked() && !rBtnAddCategoryExpense.isChecked()){
            // There isn't a type radio button selected
            tvAddCategoryMessage.setText("Please select a transaction type.");
        } else { // there is a type radio button selectd
            String description = editTextDescription.toString();
            if (description.isEmpty()){
                // The description is EMPTY
                tvAddCategoryMessage.setText("Please enter the transaction description.");
            } else {
                // there is a description
                int type;
                if (rBtnAddCategoryIncome.isChecked()){
                    type = 1; // INCOME = 1
                } else {
                    type = 2; // Expense = 2
                }
                String catDescription = editTextDescription.getText().toString();
                ((MyMoneyTrackerApp)getApplication()).AddCategory(type, catDescription);
                String msg = "The category \"" + catDescription + "\" was added.";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}