package com.example.contactmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnGetStart;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        context = this;
        btnGetStart = findViewById(R.id.btn_get_start);
        btnGetStart.setOnClickListener(v -> {
            handleClickActivityGetStart();
        });
    }

    private void handleClickActivityGetStart() {
        Intent intent = new Intent(this, ListContactActivity.class);
        startActivity(intent);
    }
}