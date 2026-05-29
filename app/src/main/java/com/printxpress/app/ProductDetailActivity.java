package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Product;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setTitle("Product Details");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long id = getIntent().getLongExtra("product_id", -1);
        if (id < 0) { finish(); return; }

        product = new DatabaseHelper(this).getProduct(id);
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tvName)).setText(product.getName());
        ((TextView) findViewById(R.id.tvCategory)).setText(product.getCategory());
        ((TextView) findViewById(R.id.tvPrice)).setText(
            String.format(Locale.US, "LKR %.2f", product.getPrice()));
        ((TextView) findViewById(R.id.tvDescription)).setText(product.getDescription());
        ((TextView) findViewById(R.id.tvMaterial)).setText(product.getMaterial());
        ((TextView) findViewById(R.id.tvSize)).setText(product.getSizeOption());

        findViewById(R.id.btnOrderNow).setOnClickListener(v -> {
            Intent i = new Intent(this, PlaceOrderActivity.class);
            i.putExtra("product_id", product.getId());
            startActivity(i);
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
