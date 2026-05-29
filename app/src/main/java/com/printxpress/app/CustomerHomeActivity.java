package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.printxpress.app.util.SessionManager;

public class CustomerHomeActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        session = new SessionManager(this);
        ((TextView) findViewById(R.id.tvGreeting)).setText("Hi, " + session.getName());

        findViewById(R.id.tileProducts).setOnClickListener(v ->
            startActivity(new Intent(this, ProductListActivity.class)));
        findViewById(R.id.tileOrders).setOnClickListener(v ->
            startActivity(new Intent(this, MyOrdersActivity.class)));
        findViewById(R.id.tilePromotions).setOnClickListener(v ->
            startActivity(new Intent(this, PromotionsActivity.class)));
        findViewById(R.id.tileGuidelines).setOnClickListener(v ->
            startActivity(new Intent(this, GuidelinesActivity.class)));
        findViewById(R.id.tileProfile).setOnClickListener(v ->
            startActivity(new Intent(this, ProfileActivity.class)));
        findViewById(R.id.btnLogout).setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Logout", (d, w) -> {
                session.clear();
                Intent i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}
