package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.printxpress.app.util.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::route, 1200);
    }

    private void route() {
        SessionManager session = new SessionManager(this);
        Intent intent;
        if (!session.isLoggedIn()) {
            intent = new Intent(this, LoginActivity.class);
        } else if (session.isAdmin()) {
            intent = new Intent(this, AdminHomeActivity.class);
        } else {
            intent = new Intent(this, CustomerHomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
