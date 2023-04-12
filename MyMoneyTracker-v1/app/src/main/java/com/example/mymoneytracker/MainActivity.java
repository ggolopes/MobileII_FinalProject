package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.menu_statement:
                //startActivity(new Intent(getApplicationContext(), StatementActivity.class));
                break;
            case R.id.menu_addTransaction:
                //startActivity(new Intent(getApplicationContext(), AddTransactionActivity.class));
                break;
            case R.id.menu_settings:
                //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            default:
                break;
        }
        return true;
    }
}

