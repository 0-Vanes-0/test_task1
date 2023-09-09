package com.example.testhotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RoomActivity extends AppCompatActivity {

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // https://run.mocky.io/v3/f9a38183-6f95-43aa-853a-9c83cbb05ecd
        setContentView(R.layout.activity_room);

        Toolbar toolbar = findViewById(R.id.toolbarRoom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Button submitRoomButton = findViewById(R.id.submitRoomButton);
        submitRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoomActivity.this, ReserveActivity.class);
            startActivity(intent);
        });

        // TODO: Сделать модуль слоя номера!
        // TODO: Сделать модуль карусели!
    }
}