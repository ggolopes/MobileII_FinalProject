package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllTransactionsActivity extends AppCompatActivity {
    private ListView listAllTransactions;
    private int userId;
    private String userName;

    private TextView tvAllUserNameMain;

    private ArrayList<LVItemTransaction> lvItems;
    private ArrayAdapter lvTransacAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String user = getIntent().getStringExtra("UserId");
        userId = Integer.valueOf(getIntent().getStringExtra("UserId"));
        userName = ((MyMoneyTrackerApp)getApplication()).GetUserName(userId);

        tvAllUserNameMain = findViewById(R.id.tvAllUserNameMain);
        tvAllUserNameMain.setText(userName);

        listAllTransactions = findViewById(R.id.lvAllTransactions);
        lvItems = ((MyMoneyTrackerApp)getApplication()).GetAllTransactions(userId);


        lvTransacAdapter = new LVItemTransactionAdapter(this, lvItems);
        listAllTransactions.setAdapter(lvTransacAdapter);
        listAllTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentViewTransaction = new Intent( AllTransactionsActivity.this, ViewTransactionActivity.class);
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
        lvItems = ((MyMoneyTrackerApp)getApplication()).GetAllTransactions(userId);
        lvTransacAdapter = new LVItemTransactionAdapter(this, lvItems);
        listAllTransactions.setAdapter(lvTransacAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            //go back to your activity / exiting the SettingsActivity
            onBackPressed();
        }
        return true;
    }

}