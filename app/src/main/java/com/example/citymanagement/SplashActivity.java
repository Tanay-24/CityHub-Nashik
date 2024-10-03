package com.example.citymanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPreferences";
    private static final String IS_FIRST_LAUNCH = "IsFirstLaunch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize the views
        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.app_name);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Apply animations
        logo.startAnimation(fadeIn);
        appName.startAnimation(slideUp);

        // Transition to the next activity after the animation ends
        new Handler().postDelayed(() -> {
            // Check if it's the first time the app is being launched
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean isFirstLaunch = preferences.getBoolean(IS_FIRST_LAUNCH, true);

            if (isFirstLaunch) {
                // If it's the first launch, show the AppInfoActivity
                Intent intent = new Intent(SplashActivity.this, AppInfoActivity.class);
                startActivity(intent);

                // Mark first launch as false
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IS_FIRST_LAUNCH, false);
                editor.apply();
            } else {
                // If it's not the first launch, go to MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

            // Close the SplashActivity so users can't return to it
            finish();
        }, 4000); // Duration for the splash screen (4 seconds)
    }
}