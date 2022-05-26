package com.example.aimech.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.aimech.R;

public class SplashScreen extends AppCompatActivity {

    //initialize Variable
    ImageView appName, splashImg;
    LottieAnimationView lottieAnimationView;

    //initialize and add splash time
    private static final int SPLASH_TIME = 4500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //initialize reference to views
        appName = findViewById(R.id.app_name_IV);
        splashImg = findViewById(R.id.splash_image);

        //set animation
        splashImg.animate().translationY(1800).setDuration(1000).setStartDelay(3500);
        appName.animate().translationY(-1800).setDuration(1000).setStartDelay(3500);

        //move the activity after Splash time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SelectionScreen.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
}