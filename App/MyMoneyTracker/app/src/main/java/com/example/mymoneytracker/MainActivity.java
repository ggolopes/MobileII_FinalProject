package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listLastTransacions;
    private int userId;
    private String userName;
    private double balOverAll;
    private double balIncome;
    private double balExpense;

    private TextView tvBalanceAmount;
    private TextView tvIncomeAmount;
    private TextView tvExpenseAmount;
    private TextView tvUserNameMain;
    private ArrayList<LVItemTransaction> lvItems;
    private ArrayAdapter lvTransacAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String user = getIntent().getStringExtra("UserId");
        userId = Integer.valueOf(getIntent().getStringExtra("UserId"));
        userName = ((MyMoneyTrackerApp)getApplication()).GetUserName(userId);

        tvBalanceAmount = findViewById(R.id.tvBalanceAmount);
        tvIncomeAmount = findViewById(R.id.tvIncomeAmount);
        tvExpenseAmount = findViewById(R.id.tvExpenseAmount);
        tvUserNameMain = findViewById(R.id.tvUserNameMain);

        tvUserNameMain.setText(userName);
        RefreshBalance();


        listLastTransacions = findViewById(R.id.lvLastTransactions);
        lvItems = ((MyMoneyTrackerApp)getApplication()).GetLastTenTransactions(userId);


        lvTransacAdapter = new LVItemTransactionAdapter(this, lvItems);
        listLastTransacions.setAdapter(lvTransacAdapter);
        listLastTransacions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentViewTransaction = new Intent( MainActivity.this, ViewTransactionActivity.class);
                intentViewTransaction.putExtra("TransactionId", ("" + lvItems.get(position).getTransacId()));
                intentViewTransaction.putExtra("UserId", ("" + userId));
                startActivity(intentViewTransaction);
            }
        });
    }

    private void RefreshBalance() {
        balIncome = ((MyMoneyTrackerApp)getApplication()).GetIncomeBalance(userId);
        balExpense = ((MyMoneyTrackerApp)getApplication()).GetExpenseBalance(userId);
        balOverAll = balIncome - balExpense;

        tvBalanceAmount.setText("" + balOverAll);
        tvIncomeAmount.setText("" + balIncome);
        tvExpenseAmount.setText("" + balExpense);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lvTransacAdapter.notifyDataSetChanged();
        lvItems = ((MyMoneyTrackerApp)getApplication()).GetLastTenTransactions(userId);
        lvTransacAdapter = new LVItemTransactionAdapter(this, lvItems);
        listLastTransacions.setAdapter(lvTransacAdapter);
        RefreshBalance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.menu_currency:
                ShowCurrencyActivity();
                break;
            case R.id.menu_statement:
                ShowAllStatmentsActivity();
                break;
            case R.id.menu_addTransaction:
                ShowAddTransactionActivity();
                break;
            case R.id.menu_addCategory:
                ShowAddCategoryActivity();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    private void ShowAddCategoryActivity() {
        startActivity(new Intent(getApplicationContext(), AddCategoryActivity.class));
    }

    private void ShowAddTransactionActivity() {
        Intent addTransactionIntent = new Intent(this, AddTransactionActivity.class);
        addTransactionIntent.putExtra("UserId", ("" + userId));
        startActivity(addTransactionIntent);
    }

    private void ShowAllStatmentsActivity() {
        Intent AllTransactionsIntent = new Intent(this, AllTransactionsActivity.class);
        AllTransactionsIntent.putExtra("UserId", ("" + userId));
        startActivity(AllTransactionsIntent);
    }

    private void ShowCurrencyActivity() {
        Intent currencyIntent = new Intent(this, CurrencyRatesActivity.class);
        startActivity(currencyIntent);
    }

    @Override
    protected void onStop() {
        startService(new Intent(getApplicationContext(), NotificationService.class));
        super.onStop();
    }


    public void DoShowAddTransactionActivity(View view) {
        ShowAddTransactionActivity();
    }

    public void DoShowAllStatmentsActivity(View view) {
        ShowAllStatmentsActivity();
    }

    public void DoShowCurrencyActivity(View view) {
        ShowCurrencyActivity();
    }
}

