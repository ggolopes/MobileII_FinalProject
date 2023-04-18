package com.example.mymoneytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvPassword;
    private TextView tvSpace;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvUserName = findViewById(R.id.tvUserName);
        tvPassword = findViewById(R.id.tvPassword);
        tvSpace = findViewById(R.id.tvSpace);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPreferences = getSharedPreferences("LastInput", MODE_PRIVATE);

    }


    public void LogIn(View view) {
        String userName = tvUserName.getText().toString();
        String password = tvPassword.getText().toString();
    }

    public void DoLogin(View view) {
        tvSpace.setText("");
        String userName = tvUserName.getText().toString();
        String password = tvPassword.getText().toString();
        if(userName.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter your USERNAME and PASSWORD.", Toast.LENGTH_SHORT).show();
        } else{
            boolean userLogged = ((MyMoneyTrackerApp)getApplication()).LoginUser(userName, password);
            if (userLogged){
                // Login Successfull
                int userId = ((MyMoneyTrackerApp)getApplication()).GetUserId(userName);
                Intent intentMain = new Intent(this, MainActivity.class);
                intentMain.putExtra("UserId", ("" + userId));
                startActivity(intentMain);
            } else { // User NOT LOGGED IN
                tvSpace.setText("Wrong User and/or Password.");
            }
        }
    }

    public void GoRegister(View view) {
        tvSpace.setText("Wrong User and/or Password.");
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentRegister);
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", tvUserName.getText().toString());
        editor.putString("password", tvPassword.getText().toString());
        editor.commit();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvUserName.setText(sharedPreferences.getString("userName",""));
        tvPassword.setText(sharedPreferences.getString("password",""));
    }
}