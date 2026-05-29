package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.User;
import com.printxpress.app.util.SessionManager;
import com.printxpress.app.util.Validator;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPhone, tilAddress, tilPassword, tilConfirm;
    private TextInputEditText etName, etEmail, etPhone, etAddress, etPassword, etConfirm;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilAddress = findViewById(R.id.tilAddress);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirm = findViewById(R.id.tilConfirm);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirm);

        findViewById(R.id.btnRegister).setOnClickListener(v -> attemptRegister());
        findViewById(R.id.tvGoLogin).setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        clearErrors();

        String name = String.valueOf(etName.getText()).trim();
        String email = String.valueOf(etEmail.getText()).trim().toLowerCase();
        String phone = String.valueOf(etPhone.getText()).trim();
        String address = String.valueOf(etAddress.getText()).trim();
        String password = String.valueOf(etPassword.getText());
        String confirm = String.valueOf(etConfirm.getText());

        boolean ok = true;
        if (!Validator.isNotEmpty(name)) { tilName.setError("Enter your name"); ok = false; }
        if (!Validator.isValidEmail(email)) { tilEmail.setError("Enter a valid email"); ok = false; }
        if (!Validator.isValidPhone(phone)) {
            tilPhone.setError("Enter a valid phone number"); ok = false;
        }
        if (!Validator.isNotEmpty(address)) { tilAddress.setError("Enter your address"); ok = false; }
        if (!Validator.isValidPassword(password)) {
            tilPassword.setError("Password must be at least 6 characters"); ok = false;
        }
        if (!password.equals(confirm)) {
            tilConfirm.setError("Passwords do not match"); ok = false;
        }
        if (!ok) return;

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPhone(phone);
        u.setAddress(address);
        u.setRole(User.ROLE_CUSTOMER);

        long id = dbHelper.registerUser(u, password);
        if (id < 0) {
            tilEmail.setError("This email is already registered");
            return;
        }
        u.setId(id);

        new SessionManager(this).saveSession(u);
        Toast.makeText(this, "Welcome, " + name + "!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, CustomerHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void clearErrors() {
        tilName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
        tilAddress.setError(null);
        tilPassword.setError(null);
        tilConfirm.setError(null);
    }
}
