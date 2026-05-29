package com.printxpress.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GuidelinesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidelines);

        setTitle("Print Guidelines & FAQ");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}
