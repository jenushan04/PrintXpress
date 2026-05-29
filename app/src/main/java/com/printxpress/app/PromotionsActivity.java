package com.printxpress.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.adapter.PromotionAdapter;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Promotion;

import java.util.ArrayList;
import java.util.List;

public class PromotionsActivity extends AppCompatActivity {

    private final List<Promotion> items = new ArrayList<>();
    private PromotionAdapter adapter;
    private TextView tvEmpty;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        setTitle("Promotions & Offers");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        RecyclerView rv = findViewById(R.id.rvPromotions);

        adapter = new PromotionAdapter(items, p -> {}, false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        load();
    }

    private void load() {
        items.clear();
        items.addAll(db.getActivePromotions());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
