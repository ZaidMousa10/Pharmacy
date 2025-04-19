package com.example.assignment1_pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Link to layout XML

        // Get the logo view
        ImageView splashLogo = findViewById(R.id.splashLogo);

        // Load animation from XML and apply to logo
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        splashLogo.startAnimation(anim);

        // Navigate to MainActivity after delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Prevent returning to splash screen
        }, 3500); // 3.5 seconds
    }
}
