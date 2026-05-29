package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.printxpress.app.adapter.ProductAdapter;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView rv;
    private ProductAdapter adapter;
    private final List<Product> data = new ArrayList<>();
    private TextView tvEmpty;
    private Spinner spnCategory;
    private EditText etSearch;
    private String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setTitle("Browse Products");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        rv = findViewById(R.id.rvProducts);
        tvEmpty = findViewById(R.id.tvEmpty);
        spnCategory = findViewById(R.id.spnCategory);
        etSearch = findViewById(R.id.etSearch);

        adapter = new ProductAdapter(data, p -> {
            Intent i = new Intent(this, ProductDetailActivity.class);
            i.putExtra("product_id", p.getId());
            startActivity(i);
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        setupFilters();
        loadProducts();
    }

    private void setupFilters() {
        List<String> cats = new ArrayList<>();
        cats.add("All");
        cats.addAll(db.getDistinctCategories());

        ArrayAdapter<String> a = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, cats);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(a);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = (String) parent.getItemAtPosition(position);
                loadProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { loadProducts(); }
        });
    }

    private void loadProducts() {
        String q = etSearch.getText().toString().trim();
        data.clear();
        data.addAll(db.getActiveProducts(currentCategory, q));
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
