package com.printxpress.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.Order;
import com.printxpress.app.model.Product;
import com.printxpress.app.util.SessionManager;
import com.printxpress.app.util.Validator;

import java.util.Locale;

public class PlaceOrderActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private Product product;

    private TextView tvProductName, tvUnitPrice, tvSubtotal, tvDesignFile;
    private TextInputLayout tilQuantity, tilAddress;
    private TextInputEditText etQuantity, etSpecifications, etCustomText, etAddress;
    private RadioGroup rgDelivery;

    private String selectedDesignUri = null;

    private final ActivityResultLauncher<Intent> filePicker = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (SecurityException ignored) {}
                    selectedDesignUri = uri.toString();
                    tvDesignFile.setText("Selected: " + uri.getLastPathSegment());
                }
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        setTitle("Place Order");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        long pid = getIntent().getLongExtra("product_id", -1);
        product = db.getProduct(pid);
        if (product == null) { finish(); return; }

        tvProductName = findViewById(R.id.tvProductName);
        tvUnitPrice = findViewById(R.id.tvUnitPrice);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDesignFile = findViewById(R.id.tvDesignFile);

        tilQuantity = findViewById(R.id.tilQuantity);
        tilAddress = findViewById(R.id.tilAddress);
        etQuantity = findViewById(R.id.etQuantity);
        etSpecifications = findViewById(R.id.etSpecifications);
        etCustomText = findViewById(R.id.etCustomText);
        etAddress = findViewById(R.id.etAddress);
        rgDelivery = findViewById(R.id.rgDelivery);

        tvProductName.setText(product.getName());
        tvUnitPrice.setText(String.format(Locale.US, "Unit price: LKR %.2f", product.getPrice()));

        etAddress.setText(session.getAddress());

        etQuantity.setText("1");
        recalcSubtotal();

        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { recalcSubtotal(); }
        });

        rgDelivery.setOnCheckedChangeListener((group, checkedId) -> {
            boolean home = checkedId == R.id.rbHomeDelivery;
            tilAddress.setVisibility(home ? View.VISIBLE : View.GONE);
        });

        findViewById(R.id.btnPickDesign).setOnClickListener(v -> openFilePicker());
        findViewById(R.id.btnSubmit).setOnClickListener(v -> submitOrder());
    }

    private void recalcSubtotal() {
        int qty = 0;
        try { qty = Integer.parseInt(String.valueOf(etQuantity.getText()).trim()); }
        catch (NumberFormatException ignored) {}
        double total = qty > 0 ? qty * product.getPrice() : 0;
        tvSubtotal.setText(String.format(Locale.US, "Total: LKR %.2f", total));
    }

    private void openFilePicker() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        String[] mimeTypes = {"image/*", "application/pdf"};
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        try {
            filePicker.launch(i);
        } catch (Exception e) {
            Toast.makeText(this, "No file picker available", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitOrder() {
        tilQuantity.setError(null);
        tilAddress.setError(null);

        String qtyStr = String.valueOf(etQuantity.getText()).trim();
        if (!Validator.isPositiveInt(qtyStr)) {
            tilQuantity.setError("Enter a positive quantity");
            return;
        }
        int quantity = Integer.parseInt(qtyStr);
        if (quantity > 10000) {
            tilQuantity.setError("Quantity too large");
            return;
        }

        String delivery = rgDelivery.getCheckedRadioButtonId() == R.id.rbHomeDelivery
            ? Order.DELIVERY_HOME : Order.DELIVERY_PICKUP;
        String address = String.valueOf(etAddress.getText()).trim();
        if (Order.DELIVERY_HOME.equals(delivery) && !Validator.isNotEmpty(address)) {
            tilAddress.setError("Address is required for home delivery");
            return;
        }

        Order o = new Order();
        o.setUserId(session.getUserId());
        o.setProductId(product.getId());
        o.setQuantity(quantity);
        o.setSpecifications(String.valueOf(etSpecifications.getText()).trim());
        o.setCustomText(String.valueOf(etCustomText.getText()).trim());
        o.setDesignFileUri(selectedDesignUri);
        o.setDeliveryType(delivery);
        o.setDeliveryAddress(Order.DELIVERY_HOME.equals(delivery) ? address : null);
        o.setTotalAmount(quantity * product.getPrice());

        long id = db.placeOrder(o);
        if (id > 0) {
            Toast.makeText(this, "Order placed! ID #" + id, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MyOrdersActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
