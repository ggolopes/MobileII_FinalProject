package com.example.mymoneytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvUserNameRegister;
    private TextView tvPasswordRegister;
    private TextView tvEmailRegister;
    private TextView tvSpaceRegister;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvUserNameRegister = findViewById(R.id.tvUserNameRegister);
        tvEmailRegister = findViewById(R.id.tvEmailRegister);
        tvPasswordRegister = findViewById(R.id.tvPasswordRegister);
        tvSpaceRegister = findViewById(R.id.tvSpaceRegister);
        btnRegister = findViewById(R.id.btnRegister);

    }

    public void SignIn(){
        finish();
    }

    public void DoRegisterUser(View view) {
        String userName = tvUserNameRegister.getText().toString();
        String userEmail = tvEmailRegister.getText().toString();
        String userPassword = tvPasswordRegister.getText().toString();
        char c = userName.charAt(0);

        if (userName.length() < 2 || !Character.isLetter(c)){
            tvSpaceRegister.setText("Enter a VALID User Name.");
        } else if (userPassword.isEmpty()) {
            tvSpaceRegister.setText("Enter a VALID Password.");
        } else if (userEmail.isEmpty()) {
            tvSpaceRegister.setText("Enter a VALID Email.");
        } else if (((MyMoneyTrackerApp)getApplication()).GetUserId(userName) > 0) {
            // The user name is already registered.
            tvSpaceRegister.setText("This user name is already registered.");
        } else {
            // The username id OK, the Password is OK, the Email is OK
            if ((((MyMoneyTrackerApp)getApplication()).AddUser(userName, userEmail, userPassword))) {
                Toast.makeText(this, "User registered successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}