package com.printxpress.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.printxpress.app.db.DatabaseHelper;
import com.printxpress.app.model.User;
import com.printxpress.app.util.SessionManager;
import com.printxpress.app.util.Validator;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> attemptLogin());
        findViewById(R.id.tvGoRegister).setOnClickListener(v ->
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = String.valueOf(etEmail.getText()).trim().toLowerCase();
        String password = String.valueOf(etPassword.getText());

        boolean ok = true;
        if (!Validator.isValidEmail(email)) {
            tilEmail.setError("Enter a valid email");
            ok = false;
        }
        if (!Validator.isValidPassword(password)) {
            tilPassword.setError("Password must be at least 6 characters");
            ok = false;
        }
        if (!ok) return;

        User user = dbHelper.authenticate(email, password);
        if (user == null) {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            return;
        }

        new SessionManager(this).saveSession(user);
        Intent intent = user.isAdmin()
            ? new Intent(this, AdminHomeActivity.class)
            : new Intent(this, CustomerHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
