package com.printxpress.app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Promotion;
import com.printxpress.app.util.Validator;

public class AdminEditPromotionActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private TextInputLayout tilTitle, tilDescription, tilDiscount, tilValidUntil;
    private TextInputEditText etTitle, etDescription, etDiscount, etValidUntil;

    private Promotion editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_promotion);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        tilTitle = findViewById(R.id.tilTitle);
        tilDescription = findViewById(R.id.tilDescription);
        tilDiscount = findViewById(R.id.tilDiscount);
        tilValidUntil = findViewById(R.id.tilValidUntil);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDiscount = findViewById(R.id.etDiscount);
        etValidUntil = findViewById(R.id.etValidUntil);

        long id = getIntent().getLongExtra("promotion_id", -1);
        if (id > 0) {
            editing = db.getPromotion(id);
            if (editing != null) {
                setTitle("Edit Promotion");
                etTitle.setText(editing.getTitle());
                etDescription.setText(editing.getDescription());
                etDiscount.setText(String.valueOf(editing.getDiscountPercent()));
                etValidUntil.setText(editing.getValidUntil());
            }
        } else {
            setTitle("Add Promotion");
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> save());
    }

    private void save() {
        tilTitle.setError(null);
        tilDescription.setError(null);
        tilDiscount.setError(null);
        tilValidUntil.setError(null);

        String title = String.valueOf(etTitle.getText()).trim();
        String description = String.valueOf(etDescription.getText()).trim();
        String discountStr = String.valueOf(etDiscount.getText()).trim();
        String validUntil = String.valueOf(etValidUntil.getText()).trim();

        boolean ok = true;
        if (!Validator.isNotEmpty(title)) { tilTitle.setError("Required"); ok = false; }
        if (!Validator.isNotEmpty(description)) { tilDescription.setError("Required"); ok = false; }
        int discount = 0;
        try {
            discount = Integer.parseInt(discountStr);
            if (discount < 0 || discount > 100) {
                tilDiscount.setError("0 - 100 only"); ok = false;
            }
        } catch (NumberFormatException e) {
            tilDiscount.setError("Enter a number"); ok = false;
        }
        if (!validUntil.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            tilValidUntil.setError("Format: YYYY-MM-DD"); ok = false;
        }
        if (!ok) return;

        Promotion p = editing == null ? new Promotion() : editing;
        p.setTitle(title);
        p.setDescription(description);
        p.setDiscountPercent(discount);
        p.setValidUntil(validUntil);
        if (editing == null) p.setActive(true);

        boolean success;
        if (editing == null) {
            success = db.addPromotion(p) > 0;
        } else {
            success = db.updatePromotion(p);
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
