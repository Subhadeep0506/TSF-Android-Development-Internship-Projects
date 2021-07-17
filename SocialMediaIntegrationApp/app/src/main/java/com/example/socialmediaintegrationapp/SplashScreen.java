package com.example.socialmediaintegrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        LinearLayout layout = (LinearLayout) findViewById(R.id.splashScreenBg);
        ImageView ivLogo = (ImageView) findViewById(R.id.ivLogo);
        TextView splashScreenText = (TextView) findViewById(R.id.splashScreenText);

        layout.animate().alpha(1).setDuration(800);
        ivLogo.animate().alpha(1).setStartDelay(1000).setDuration(500);
        splashScreenText.animate().alpha(1).setStartDelay(1000).setDuration(200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
                finish();
            }
        }, 3000);
    }
}