package com.example.testhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        DataLoader.load(() -> {
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}