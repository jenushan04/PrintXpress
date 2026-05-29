package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Order;
import com.printxpress.app.util.SessionManager;

import java.util.Locale;

public class AdminHomeActivity extends AppCompatActivity {

    private SessionManager session;
    private DatabaseHelper db;

    private TextView tvTotalOrders, tvPendingOrders, tvProcessingOrders, tvCompletedOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        session = new SessionManager(this);
        db = new DatabaseHelper(this);

        ((TextView) findViewById(R.id.tvGreeting)).setText("Hi, " + session.getName());

        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);
        tvProcessingOrders = findViewById(R.id.tvProcessingOrders);
        tvCompletedOrders = findViewById(R.id.tvCompletedOrders);

        findViewById(R.id.tileOrders).setOnClickListener(v ->
            startActivity(new Intent(this, AdminOrdersActivity.class)));
        findViewById(R.id.tileProducts).setOnClickListener(v ->
            startActivity(new Intent(this, AdminProductsActivity.class)));
        findViewById(R.id.tilePromotions).setOnClickListener(v ->
            startActivity(new Intent(this, AdminPromotionsActivity.class)));
        findViewById(R.id.tileCustomers).setOnClickListener(v ->
            startActivity(new Intent(this, AdminCustomersActivity.class)));
        findViewById(R.id.btnLogout).setOnClickListener(v -> confirmLogout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStats();
    }

    private void refreshStats() {
        tvTotalOrders.setText(String.format(Locale.US, "%d", db.countOrdersByStatus(null)));
        tvPendingOrders.setText(String.format(Locale.US, "%d", db.countOrdersByStatus(Order.STATUS_PENDING)));
        tvProcessingOrders.setText(String.format(Locale.US, "%d",
            db.countOrdersByStatus(Order.STATUS_PROCESSING)
                + db.countOrdersByStatus(Order.STATUS_PRINTING)));
        tvCompletedOrders.setText(String.format(Locale.US, "%d",
            db.countOrdersByStatus(Order.STATUS_COMPLETED)));
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Log out of the admin dashboard?")
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
