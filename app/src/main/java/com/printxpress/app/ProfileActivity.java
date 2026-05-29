package com.printxpress.app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.User;
import com.printxpress.app.util.PasswordUtil;
import com.printxpress.app.util.SessionManager;
import com.printxpress.app.util.Validator;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;

    private TextInputLayout tilName, tilPhone, tilAddress;
    private TextInputEditText etName, etEmail, etPhone, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("My Profile");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        tilName = findViewById(R.id.tilName);
        tilPhone = findViewById(R.id.tilPhone);
        tilAddress = findViewById(R.id.tilAddress);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);

        User u = db.findUserById(session.getUserId());
        if (u == null) { finish(); return; }
        etName.setText(u.getName());
        etEmail.setText(u.getEmail());
        etPhone.setText(u.getPhone());
        etAddress.setText(u.getAddress());

        findViewById(R.id.btnSave).setOnClickListener(v -> save());
        findViewById(R.id.btnChangePassword).setOnClickListener(v -> showChangePasswordDialog());
    }

    private void save() {
        tilName.setError(null);
        tilPhone.setError(null);
        tilAddress.setError(null);

        String name = String.valueOf(etName.getText()).trim();
        String phone = String.valueOf(etPhone.getText()).trim();
        String address = String.valueOf(etAddress.getText()).trim();

        boolean ok = true;
        if (!Validator.isNotEmpty(name)) { tilName.setError("Enter your name"); ok = false; }
        if (!Validator.isValidPhone(phone)) { tilPhone.setError("Invalid phone"); ok = false; }
        if (!Validator.isNotEmpty(address)) { tilAddress.setError("Enter your address"); ok = false; }
        if (!ok) return;

        if (db.updateUserProfile(session.getUserId(), name, phone, address)) {
            User u = db.findUserById(session.getUserId());
            session.saveSession(u);
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showChangePasswordDialog() {
        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        TextInputEditText etCurrent = view.findViewById(R.id.etCurrentPassword);
        TextInputEditText etNew = view.findViewById(R.id.etNewPassword);

        new AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(view)
            .setPositiveButton("Update", (d, w) -> {
                String current = String.valueOf(etCurrent.getText());
                String newPass = String.valueOf(etNew.getText());
                if (!Validator.isValidPassword(newPass)) {
                    Toast.makeText(this, "New password must be 6+ characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                User u = db.findUserById(session.getUserId());
                if (u == null || !PasswordUtil.verify(current, u.getPasswordHash())) {
                    Toast.makeText(this, "Current password is wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (db.changePassword(session.getUserId(), newPass)) {
                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
