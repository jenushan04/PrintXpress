package com.printxpress.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.adapter.CustomerAdapter;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminCustomersActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private final List<User> items = new ArrayList<>();
    private CustomerAdapter adapter;
    private TextView tvEmpty, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customers);

        setTitle("Customers");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        tvTotal = findViewById(R.id.tvTotal);
        RecyclerView rv = findViewById(R.id.rvCustomers);

        adapter = new CustomerAdapter(items);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        load();
    }

    private void load() {
        items.clear();
        items.addAll(db.getAllCustomers());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        tvTotal.setText(String.format(Locale.US, "Total registered: %d", items.size()));
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
