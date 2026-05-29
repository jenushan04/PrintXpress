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

import com.printxpress.app.adapter.PromotionAdapter;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Promotion;

import java.util.ArrayList;
import java.util.List;

public class AdminPromotionsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private final List<Promotion> items = new ArrayList<>();
    private PromotionAdapter adapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_promotions);

        setTitle("Manage Promotions");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        RecyclerView rv = findViewById(R.id.rvPromotions);

        adapter = new PromotionAdapter(items, this::showPromoActions, true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        findViewById(R.id.btnAddPromotion).setOnClickListener(v -> {
            Intent i = new Intent(this, AdminEditPromotionActivity.class);
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
        items.addAll(db.getAllPromotionsForAdmin());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showPromoActions(Promotion p) {
        String toggleLabel = p.isActive() ? "Deactivate" : "Activate";
        String[] options = {"Edit", toggleLabel, "Delete"};

        new AlertDialog.Builder(this)
            .setTitle(p.getTitle())
            .setItems(options, (d, which) -> {
                if (which == 0) {
                    Intent i = new Intent(this, AdminEditPromotionActivity.class);
                    i.putExtra("promotion_id", p.getId());
                    startActivity(i);
                } else if (which == 1) {
                    p.setActive(!p.isActive());
                    if (db.updatePromotion(p)) {
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                        load();
                    }
                } else {
                    confirmDelete(p);
                }
            })
            .show();
    }

    private void confirmDelete(Promotion p) {
        new AlertDialog.Builder(this)
            .setTitle("Delete \"" + p.getTitle() + "\"?")
            .setMessage("This permanently removes the promotion.")
            .setPositiveButton("Delete", (d, w) -> {
                if (db.deletePromotion(p.getId())) {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    load();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
