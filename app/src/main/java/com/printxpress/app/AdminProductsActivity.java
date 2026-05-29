package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.adapter.ProductAdapter;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AdminProductsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private final List<Product> items = new ArrayList<>();
    private ProductAdapter adapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);

        setTitle("Manage Products");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        RecyclerView rv = findViewById(R.id.rvProducts);

        adapter = new ProductAdapter(items, this::showProductActions, true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        findViewById(R.id.btnAddProduct).setOnClickListener(v -> {
            Intent i = new Intent(this, AdminEditProductActivity.class);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        items.clear();
        items.addAll(db.getAllProductsForAdmin());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showProductActions(Product p) {
        String[] options = p.isActive()
            ? new String[]{"Edit", "Deactivate"}
            : new String[]{"Edit", "Reactivate"};

        new AlertDialog.Builder(this)
            .setTitle(p.getName())
            .setItems(options, (d, which) -> {
                if (which == 0) {
                    Intent i = new Intent(this, AdminEditProductActivity.class);
                    i.putExtra("product_id", p.getId());
                    startActivity(i);
                } else {
                    if (p.isActive()) {
                        confirmDeactivate(p);
                    } else {
                        p.setActive(true);
                        if (db.updateProduct(p)) {
                            Toast.makeText(this, "Reactivated", Toast.LENGTH_SHORT).show();
                            load();
                        }
                    }
                }
            })
            .show();
    }

    private void confirmDeactivate(Product p) {
        new AlertDialog.Builder(this)
            .setTitle("Deactivate \"" + p.getName() + "\"?")
            .setMessage("Customers won't see this product anymore, but past orders stay intact.")
            .setPositiveButton("Deactivate", (d, w) -> {
                if (db.deleteProduct(p.getId())) {
                    Toast.makeText(this, "Deactivated", Toast.LENGTH_SHORT).show();
                    load();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
