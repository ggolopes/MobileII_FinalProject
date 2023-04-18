package com.example.mymoneytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class CurrencyRatesActivity extends AppCompatActivity {

    private Boolean doOnPause = true;
    private String ratesData;
    private RadioButton rBtnFromUSD;
    private RadioButton rBtnFromCAD;
    private RadioButton rBtnFromBRL;
    private RadioButton rBtnFromINR;
    private RadioButton rBtnFromCNY;
    private RadioButton rBtnToUSD;
    private RadioButton rBtnToCAD;
    private RadioButton rBtnToBRL;
    private RadioButton rBtnToINR;
    private RadioButton rBtnToCNY;

    private double usCAD = 0;
    private double usBRL = 0;
    private double usINR = 0;
    private double usCNY = 0;
    private SharedPreferences sharedPreferences;

    private TextView tvCurrencyRateValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_rates);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        rBtnFromUSD = findViewById(R.id.rBtnFromUSD);
        rBtnFromCAD = findViewById(R.id.rBtnFromCAD);
        rBtnFromBRL = findViewById(R.id.rBtnFromBRL);
        rBtnFromINR = findViewById(R.id.rBtnFromINR);
        rBtnFromCNY = findViewById(R.id.rBtnFromCNY);
        rBtnToUSD = findViewById(R.id.rBtnToUSD);
        rBtnToCAD = findViewById(R.id.rBtnToCAD);
        rBtnToBRL = findViewById(R.id.rBtnToBRL);
        rBtnToINR = findViewById(R.id.rBtnToINR);
        rBtnToCNY = findViewById(R.id.rBtnToCNY);
        tvCurrencyRateValue = findViewById(R.id.tvCurrencyRateValue);

        sharedPreferences = getSharedPreferences("LastInput", MODE_PRIVATE);

        ratesData = "";
        ReadRates readRates = new ReadRates();
        readRates.execute("https://economia.awesomeapi.com.br/last/USD-CAD,USD-BRL,USD-INR,USD-CNY");

        if (ratesData.isEmpty()){
            tvCurrencyRateValue.setText("Choose the currencies.");
        } else {
            String str_usCAD = null;
            String str_usBRL = null;
            String str_usINR = null;
            String str_usCNY = null;

            try {
                str_usCAD = new JSONObject(ratesData).getJSONObject("USDCAD").getString("bid");
                str_usBRL = new JSONObject(ratesData).getJSONObject("USDBRL").getString("bid");
                str_usINR = new JSONObject(ratesData).getJSONObject("USDINR").getString("bid");
                str_usCNY = new JSONObject(ratesData).getJSONObject("USDCNY").getString("bid");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            usCAD = Double.parseDouble(str_usCAD);
            usBRL = Double.parseDouble(str_usBRL);
            usINR = Double.parseDouble(str_usINR);
            usCNY = Double.parseDouble(str_usCNY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            //go back to your activity / exiting the SettingsActivity
            CleanAllFieldsCurrencyRates();
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onPause() {
        if (doOnPause){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rBtnFromUSD", rBtnFromUSD.isChecked());
            editor.putBoolean("rBtnFromCAD", rBtnFromCAD.isChecked());
            editor.putBoolean("rBtnFromBRL", rBtnFromBRL.isChecked());
            editor.putBoolean("rBtnFromINR", rBtnFromINR.isChecked());
            editor.putBoolean("rBtnFromCNY", rBtnFromCNY.isChecked());
            editor.putBoolean("rBtnToUSD", rBtnToUSD.isChecked());
            editor.putBoolean("rBtnToCAD", rBtnToCAD.isChecked());
            editor.putBoolean("rBtnToBRL", rBtnToBRL.isChecked());
            editor.putBoolean("rBtnToINR", rBtnToINR.isChecked());
            editor.putBoolean("rBtnToCNY", rBtnToCNY.isChecked());
            editor.putString("CurrencyRateValue", tvCurrencyRateValue.getText().toString());
            editor.commit();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        rBtnFromUSD.setChecked(sharedPreferences.getBoolean("rBtnFromUSD", false));
        rBtnFromCAD.setChecked(sharedPreferences.getBoolean("rBtnFromCAD", false));
        rBtnFromBRL.setChecked(sharedPreferences.getBoolean("rBtnFromBRL", false));
        rBtnFromINR.setChecked(sharedPreferences.getBoolean("rBtnFromINR", false));
        rBtnFromCNY.setChecked(sharedPreferences.getBoolean("rBtnFromCNY", false));
        rBtnToUSD.setChecked(sharedPreferences.getBoolean("rBtnToUSD", false));
        rBtnToCAD.setChecked(sharedPreferences.getBoolean("rBtnToCAD", false));
        rBtnToBRL.setChecked(sharedPreferences.getBoolean("rBtnToBRL", false));
        rBtnToINR.setChecked(sharedPreferences.getBoolean("rBtnToINR", false));
        rBtnToCNY.setChecked(sharedPreferences.getBoolean("rBtnToCNY", false));
        tvCurrencyRateValue.setText(sharedPreferences.getString("CurrencyRateValue", ""));
    }

    public void CleanAllFieldsCurrencyRates(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("rBtnFromUSD");
        editor.remove("rBtnFromCAD");
        editor.remove("rBtnFromBRL");
        editor.remove("rBtnFromINR");
        editor.remove("rBtnFromCNY");
        editor.remove("rBtnToUSD");
        editor.remove("rBtnToCAD");
        editor.remove("rBtnToBRL");
        editor.remove("rBtnToINR");
        editor.remove("rBtnToCNY");
        editor.remove("CurrencyRateValue");
        editor.commit();
        doOnPause = false;

    }

    public void ChangeCurrencyFrom(View view) {
        if((RadioButton)view == rBtnFromUSD){
            if (rBtnToUSD.isChecked()){
                rBtnToCAD.setChecked(true);
            }
            SetAllEnable();
            rBtnToUSD.setEnabled(false);
        } else if ((RadioButton)view == rBtnFromCAD) {
            if (rBtnToCAD.isChecked()){
                rBtnToBRL.setChecked(true);
            }
            SetAllEnable();
            rBtnToCAD.setEnabled(false);

        } else if ((RadioButton)view == rBtnFromBRL) {
            if (rBtnToBRL.isChecked()){
                rBtnToCAD.setChecked(true);
            }
            SetAllEnable();
            rBtnToBRL.setEnabled(false);

        } else if ((RadioButton)view == rBtnFromINR) {
            if (rBtnToINR.isChecked()){
                rBtnToBRL.setChecked(true);
            }
            SetAllEnable();
            rBtnToINR.setEnabled(false);

        } else if ((RadioButton)view == rBtnFromCNY) {
            if (rBtnToCNY.isChecked()){
                rBtnToBRL.setChecked(true);
            }
            SetAllEnable();
            rBtnToCNY.setEnabled(false);
        }
        tvCurrencyRateValue.setText(DoCheckRate());
    }

    private String DoCheckRate() {
        String result = "";

        if (ratesData.isEmpty()){
//            ReadRates readRates = new ReadRates();
//            readRates.execute("https://economia.awesomeapi.com.br/last/USD-CAD,USD-BRL,USD-INR,USD-CNY");
            tvCurrencyRateValue.setText("There is NO CONNECTIVITY.");
        } else {
            String str_usCAD = null;
            String str_usBRL = null;
            String str_usINR = null;
            String str_usCNY = null;

            try {
                str_usCAD = new JSONObject(ratesData).getJSONObject("USDCAD").getString("bid");
                str_usBRL = new JSONObject(ratesData).getJSONObject("USDBRL").getString("bid");
                str_usINR = new JSONObject(ratesData).getJSONObject("USDINR").getString("bid");
                str_usCNY = new JSONObject(ratesData).getJSONObject("USDCNY").getString("bid");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            usCAD = Double.parseDouble(str_usCAD);
            usBRL = Double.parseDouble(str_usBRL);
            usINR = Double.parseDouble(str_usINR);
            usCNY = Double.parseDouble(str_usCNY);

            if (rBtnFromUSD.isChecked()){ //                From USD
                if (rBtnToCAD.isChecked()) { //                 To CAD
                    result = "1.00 US$ = " + String.format("%.4f", usCAD) + " CAD$";
                } else if (rBtnToBRL.isChecked()){//            To BRL
                    result = "1.00 US$ = " + String.format("%.4f", usBRL) + " R$";
                } else if (rBtnToINR.isChecked()){//            To INR
                    result = "1.00 US$ = " + String.format("%.4f", usINR) + " ₹";
                } else if (rBtnToCNY.isChecked()){//            To CNY
                    result = "1.00 US$ = " + String.format("%.4f", usCNY) + " ¥";
                }
            } else if (rBtnFromCAD.isChecked()){ //        From CAD
                if (rBtnToUSD.isChecked()) { //                 To USD
                    result = "1.00 CAD$ = " + String.format("%.4f", (1/usCAD)) + " US$";
                } else if (rBtnToBRL.isChecked()){//            To BRL
                    result = "1.00 CAD$ = " + String.format("%.4f", (usBRL/usCAD)) + " R$";
                } else if (rBtnToINR.isChecked()){//            To INR
                    result = "1.00 CAD$ = " + String.format("%.4f", (usINR/usCAD)) + " ₹";
                } else if (rBtnToCNY.isChecked()){//            To CNY
                    result = "1.00 CAD$ = " + String.format("%.4f", (usCNY/usCAD)) + " ¥";
                }
            } else if (rBtnFromBRL.isChecked()){ //        From BRL
                if (rBtnToUSD.isChecked()) { //                 To USD
                    result = "1.00 R$ = " + String.format("%.4f", (1/usBRL)) + " US$";
                } else if (rBtnToCAD.isChecked()){//            To CAD
                    result = "1.00 R$ = " + String.format("%.4f", (usCAD/usBRL)) + " CAD$";
                } else if (rBtnToINR.isChecked()){//            To INR
                    result = "1.00 R$ = " + String.format("%.4f", (usINR/usBRL)) + " ₹";
                } else if (rBtnToCNY.isChecked()){//            To CNY
                    result = "1.00 R$ = " + String.format("%.4f", (usCNY/usBRL)) + " ¥";
                }
            } else if (rBtnFromINR.isChecked()){ //        From INR
                if (rBtnToUSD.isChecked()) { //                 To USD
                    result = "1.00 ₹ = " + String.format("%.4f", (1/usINR)) + " US$";
                } else if (rBtnToBRL.isChecked()){//            To BRL
                    result = "1.00 ₹ = " + String.format("%.4f", (usBRL/usINR)) + " R$";
                } else if (rBtnToINR.isChecked()){//            To CAD
                    result = "1.00 ₹ = " + String.format("%.4f", (usCAD/usINR)) + " CAD$";
                } else if (rBtnToCNY.isChecked()){//            To CNY
                    result = "1.00 ₹ = " + String.format("%.4f", (usCNY/usINR)) + " ¥";
                }
            } else if (rBtnFromINR.isChecked()){ //        From CNY
                if (rBtnToUSD.isChecked()) { //                 To USD
                    result = "1.00 ¥ = " + String.format("%.4f", (1/usCNY)) + " US$";
                } else if (rBtnToBRL.isChecked()){//            To BRL
                    result = "1.00 ¥ = " + String.format("%.4f", (usBRL/usCNY)) + " R$";
                } else if (rBtnToINR.isChecked()){//            To CAD
                    result = "1.00 ¥ = " + String.format("%.4f", (usCAD/usCNY)) + " CAD$";
                } else if (rBtnToCNY.isChecked()){//            To INR
                    result = "1.00 ¥ = " + String.format("%.4f", (usINR/usCNY)) + " ₹";
                }
            }
        }
        return result;
    }

    public void SetAllEnable(){
        rBtnFromUSD.setEnabled(true);
        rBtnFromCAD.setEnabled(true);
        rBtnFromBRL.setEnabled(true);
        rBtnFromINR.setEnabled(true);
        rBtnFromCNY.setEnabled(true);
        rBtnToUSD.setEnabled(true);
        rBtnToCAD.setEnabled(true);
        rBtnToBRL.setEnabled(true);
        rBtnToINR.setEnabled(true);
        rBtnToCNY.setEnabled(true);
    }
    public void ChangeCurrencyTo(View view) {
        tvCurrencyRateValue.setText(DoCheckRate());
    }

    private class ReadRates extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = ConnectToGetRates.getRates(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            ratesData = s;
        }
    }

}