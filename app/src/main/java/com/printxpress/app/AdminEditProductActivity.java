package com.printxpress.app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Product;
import com.printxpress.app.util.Validator;

public class AdminEditProductActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private TextInputLayout tilName, tilCategory, tilDescription, tilMaterial, tilSize, tilPrice;
    private TextInputEditText etName, etCategory, etDescription, etMaterial, etSize, etPrice;

    private Product editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tilName = findViewById(R.id.tilName);
        tilCategory = findViewById(R.id.tilCategory);
        tilDescription = findViewById(R.id.tilDescription);
        tilMaterial = findViewById(R.id.tilMaterial);
        tilSize = findViewById(R.id.tilSize);
        tilPrice = findViewById(R.id.tilPrice);

        etName = findViewById(R.id.etName);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        etMaterial = findViewById(R.id.etMaterial);
        etSize = findViewById(R.id.etSize);
        etPrice = findViewById(R.id.etPrice);

        long id = getIntent().getLongExtra("product_id", -1);
        if (id > 0) {
            editing = db.getProduct(id);
            if (editing != null) {
                setTitle("Edit Product");
                etName.setText(editing.getName());
                etCategory.setText(editing.getCategory());
                etDescription.setText(editing.getDescription());
                etMaterial.setText(editing.getMaterial());
                etSize.setText(editing.getSizeOption());
                etPrice.setText(String.valueOf(editing.getPrice()));
            }
        } else {
            setTitle("Add Product");
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> save());
    }

    private void save() {
        tilName.setError(null);
        tilCategory.setError(null);
        tilDescription.setError(null);
        tilMaterial.setError(null);
        tilSize.setError(null);
        tilPrice.setError(null);

        String name = String.valueOf(etName.getText()).trim();
        String category = String.valueOf(etCategory.getText()).trim();
        String description = String.valueOf(etDescription.getText()).trim();
        String material = String.valueOf(etMaterial.getText()).trim();
        String size = String.valueOf(etSize.getText()).trim();
        String priceStr = String.valueOf(etPrice.getText()).trim();

        boolean ok = true;
        if (!Validator.isNotEmpty(name)) { tilName.setError("Required"); ok = false; }
        if (!Validator.isNotEmpty(category)) { tilCategory.setError("Required"); ok = false; }
        if (!Validator.isNotEmpty(description)) { tilDescription.setError("Required"); ok = false; }
        if (!Validator.isNotEmpty(material)) { tilMaterial.setError("Required"); ok = false; }
        if (!Validator.isNotEmpty(size)) { tilSize.setError("Required"); ok = false; }
        if (!Validator.isPositiveDouble(priceStr)) { tilPrice.setError("Enter a positive price"); ok = false; }
        if (!ok) return;

        Product p = editing == null ? new Product() : editing;
        p.setName(name);
        p.setCategory(category);
        p.setDescription(description);
        p.setMaterial(material);
        p.setSizeOption(size);
        p.setPrice(Double.parseDouble(priceStr));
        if (editing == null) p.setActive(true);

        boolean success;
        if (editing == null) {
            success = db.addProduct(p) > 0;
        } else {
            success = db.updateProduct(p);
        }

        if (success) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
