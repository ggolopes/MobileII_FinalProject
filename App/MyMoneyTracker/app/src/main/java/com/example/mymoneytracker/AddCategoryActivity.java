package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getSharedPreferences("LastInput", MODE_PRIVATE);

        editTextDescription = findViewById(R.id.editTextAddCategoryDescription);
        rBtnAddCategoryIncome = findViewById(R.id.rBtnAddCategoryIncome);
        rBtnAddCategoryExpense = findViewById(R.id.rBtnAddCategoryExpense);
        tvAddCategoryMessage = findViewById(R.id.tvAddCategoryMessage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            //go back to your activity / exiting the SettingsActivity
            ClearFieldsAddCategory();
            onBackPressed();
        }
        return true;
    }

    private void ClearFieldsAddCategory() {
        editTextDescription.setText("");
        rBtnAddCategoryIncome.setChecked(false);
        rBtnAddCategoryExpense.setChecked(false);
        tvAddCategoryMessage.setText("");
    }

    public void DoAddCategory(View view) {
        tvAddCategoryMessage.setText("");
        if (!rBtnAddCategoryIncome.isChecked() && !rBtnAddCategoryExpense.isChecked()){
            // There isn't a type radio button selected
            tvAddCategoryMessage.setText("Please select a transaction type.");
        } else { // there is a type radio button selectd
            String description = editTextDescription.getText().toString();
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
                ClearFieldsAddCategory();
                finish();
            }
        }
    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("catDescription", editTextDescription.getText().toString());
        editor.putBoolean("catIncome", rBtnAddCategoryIncome.isChecked());
        editor.putBoolean("catExpense", rBtnAddCategoryExpense.isChecked());
        editor.putString("catMessage", tvAddCategoryMessage.getText().toString());
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        editTextDescription.setText(sharedPreferences.getString("catDescription", ""));
        tvAddCategoryMessage.setText(sharedPreferences.getString("catMessage", ""));
        if(sharedPreferences.contains("catIncome")){
            rBtnAddCategoryIncome.setChecked(sharedPreferences.getBoolean("catIncome", false));
        }
        if(sharedPreferences.contains("catExpense")){
            rBtnAddCategoryExpense.setChecked(sharedPreferences.getBoolean("catExpense", false));
        }
    }


}