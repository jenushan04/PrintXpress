package com.printxpress.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.adapter.OrderAdapter;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrdersActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private final List<Order> items = new ArrayList<>();
    private OrderAdapter adapter;
    private TextView tvEmpty;
    private Spinner spnFilter;
    private String currentFilter = "All";

    private static final String[] FILTERS = {
        "All", Order.STATUS_PENDING, Order.STATUS_PROCESSING,
        Order.STATUS_PRINTING, Order.STATUS_READY,
        Order.STATUS_COMPLETED, Order.STATUS_CANCELLED
    };

    private static final String[] NEW_STATUSES = {
        Order.STATUS_PENDING, Order.STATUS_PROCESSING, Order.STATUS_PRINTING,
        Order.STATUS_READY, Order.STATUS_COMPLETED, Order.STATUS_CANCELLED
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        setTitle("Manage Orders");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        spnFilter = findViewById(R.id.spnFilter);
        RecyclerView rv = findViewById(R.id.rvOrders);

        adapter = new OrderAdapter(items, this::showOrderDialog, true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, Arrays.asList(FILTERS));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilter.setAdapter(spinnerAdapter);
        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilter = FILTERS[position];
                load();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        items.clear();
        items.addAll(db.getAllOrders(currentFilter));
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showOrderDialog(Order o) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US);
        StringBuilder sb = new StringBuilder();
        sb.append("Customer: ").append(o.getCustomerName()).append("\n");
        sb.append("Product: ").append(o.getProductName()).append("\n");
        sb.append("Quantity: ").append(o.getQuantity()).append("\n");
        sb.append("Total: LKR ").append(String.format(Locale.US, "%.2f", o.getTotalAmount())).append("\n");
        sb.append("Delivery: ").append(Order.DELIVERY_HOME.equals(o.getDeliveryType())
            ? "Home Delivery" : "Store Pickup").append("\n");
        if (o.getDeliveryAddress() != null && !o.getDeliveryAddress().isEmpty()) {
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

        new AlertDialog.Builder(this)
            .setTitle("Order #" + o.getId())
            .setMessage(sb.toString())
            .setPositiveButton("Update Status", (d, w) -> showStatusPicker(o))
            .setNegativeButton("Close", null)
            .show();
    }

    private void showStatusPicker(Order o) {
        new AlertDialog.Builder(this)
            .setTitle("Set new status")
            .setItems(NEW_STATUSES, (d, which) -> {
                String newStatus = NEW_STATUSES[which];
                if (db.updateOrderStatus(o.getId(), newStatus)) {
                    Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    load();
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
