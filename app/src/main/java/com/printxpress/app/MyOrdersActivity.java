package com.printxpress.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.adapter.OrderAdapter;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Order;
import com.printxpress.app.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyOrdersActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private final List<Order> items = new ArrayList<>();
    private OrderAdapter adapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        setTitle("My Orders");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        RecyclerView rv = findViewById(R.id.rvOrders);

        adapter = new OrderAdapter(items, this::showOrderDialog, false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        items.clear();
        items.addAll(db.getOrdersForUser(session.getUserId()));
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showOrderDialog(Order o) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US);
        StringBuilder sb = new StringBuilder();
        sb.append("Product: ").append(o.getProductName()).append("\n");
        sb.append("Quantity: ").append(o.getQuantity()).append("\n");
        sb.append("Total: LKR ").append(String.format(Locale.US, "%.2f", o.getTotalAmount())).append("\n");
        sb.append("Delivery: ").append(Order.DELIVERY_HOME.equals(o.getDeliveryType())
            ? "Home Delivery" : "Store Pickup").append("\n");
        if (Order.DELIVERY_HOME.equals(o.getDeliveryType()) && o.getDeliveryAddress() != null) {
            sb.append("Address: ").append(o.getDeliveryAddress()).append("\n");
        }
        if (o.getSpecifications() != null && !o.getSpecifications().isEmpty()) {
            sb.append("Specs: ").append(o.getSpecifications()).append("\n");
        }
        if (o.getCustomText() != null && !o.getCustomText().isEmpty()) {
            sb.append("Text: ").append(o.getCustomText()).append("\n");
        }
        if (o.getDesignFileUri() != null && !o.getDesignFileUri().isEmpty()) {
            sb.append("Design uploaded: yes\n");
        }
        sb.append("Status: ").append(o.getStatus()).append("\n");
        sb.append("Placed: ").append(fmt.format(new Date(o.getCreatedAt())));

        AlertDialog.Builder b = new AlertDialog.Builder(this)
            .setTitle("Order #" + o.getId())
            .setMessage(sb.toString())
            .setNegativeButton("Close", null);

        if (o.canBeCancelledByCustomer()) {
            b.setPositiveButton("Cancel Order", (d, w) -> confirmCancel(o));
        }
        b.show();
    }

    private void confirmCancel(Order o) {
        new AlertDialog.Builder(this)
            .setTitle("Cancel order?")
            .setMessage("This will cancel order #" + o.getId() + ". Continue?")
            .setPositiveButton("Yes, cancel", (d, w) -> {
                if (db.updateOrderStatus(o.getId(), Order.STATUS_CANCELLED)) {
                    Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
                    load();
                } else {
                    Toast.makeText(this, "Cancellation failed", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Keep order", null)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
