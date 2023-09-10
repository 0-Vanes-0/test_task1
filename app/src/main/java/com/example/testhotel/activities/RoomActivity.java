package com.example.testhotel.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testhotel.DataLoader;
import com.example.testhotel.R;
import com.example.testhotel.RoomsAdapter;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // TODO: Сделать модуль слоя номера!
        // TODO: Сделать модуль карусели!
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onStart() {
        super.onStart();
        Toolbar toolbar = findViewById(R.id.toolbarRoom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.roomsRecycler);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter<RoomsAdapter.ViewHolder> adapter = new RoomsAdapter(DataLoader.getRoomsInfo(), RoomActivity.this);
        recyclerView.setAdapter(adapter);
    }
}