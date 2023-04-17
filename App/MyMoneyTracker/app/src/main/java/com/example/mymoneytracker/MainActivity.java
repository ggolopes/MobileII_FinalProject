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
        balIncome = ((MyMoneyTrackerApp)getApplication()).GetIncomeBalance(userId);
        balExpense = ((MyMoneyTrackerApp)getApplication()).GetExpenseBalance(userId);
        balOverAll = balIncome - balExpense;

        tvBalanceAmount.setText("" + balOverAll);
        tvIncomeAmount.setText("" + balIncome);
        tvExpenseAmount.setText("" + balExpense);


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

    @Override
    protected void onResume() {
        super.onResume();
        lvTransacAdapter.notifyDataSetChanged();
        lvItems = ((MyMoneyTrackerApp)getApplication()).GetLastTenTransactions(userId);
        lvTransacAdapter = new LVItemTransactionAdapter(this, lvItems);
        listLastTransacions.setAdapter(lvTransacAdapter);
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
                Intent currencyIntent = new Intent(this, CurrencyRatesActivity.class);
                startActivity(currencyIntent);
                break;
            case R.id.menu_statement:
                Intent AllTransactionsIntent = new Intent(this, AllTransactionsActivity.class);
                AllTransactionsIntent.putExtra("UserId", ("" + userId));
                startActivity(AllTransactionsIntent);
                break;
            case R.id.menu_addTransaction:
                Intent addTransactionIntent = new Intent(this, AddTransactionActivity.class);
                addTransactionIntent.putExtra("UserId", ("" + userId));
                startActivity(addTransactionIntent);
                break;
            case R.id.menu_addCategory:
                startActivity(new Intent(getApplicationContext(), AddCategoryActivity.class));
                break;
            case R.id.menu_settings:
                //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        startService(new Intent(getApplicationContext(), NotificationService.class));
        super.onStop();
    }


}

