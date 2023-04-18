package com.example.mymoneytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private ImageView appLogo;
    private ImageView devbrosLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appLogo = findViewById(R.id.ivMyMoneyTrackLogo);
        devbrosLogo = findViewById(R.id.ivDevBRosLogo);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation anime = new AlphaAnimation(0.0f, 1.0f);
        anime.setDuration(4000);
        anime.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        appLogo.setVisibility(View.VISIBLE);
        appLogo.setAlpha(1.0f);
        devbrosLogo.setVisibility(View.VISIBLE);
        devbrosLogo.setAlpha(1.0f);
        devbrosLogo.startAnimation(anime);
        appLogo.startAnimation(anime);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        }, 7000);
    }
}