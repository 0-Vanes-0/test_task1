package com.example.testhotel.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testhotel.DataLoader;
import com.example.testhotel.R;

public class MainActivity extends AppCompatActivity {

    private ImageView hotelImageView;
    private TextView nameText;
    private TextView addressText;
    private TextView minimalPriceText;
    private TextView forWhatText;
    private TextView ratingText;
    private TextView ratingNameText;
    private TextView peculiarityText;
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onStart() {
        super.onStart();
        Button chooseRoomButton = findViewById(R.id.chooseRoomButton);
        chooseRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoomActivity.class);
            startActivity(intent);
        });

        hotelImageView = findViewById(R.id.imageView);

        nameText = findViewById(R.id.nameText);
        addressText = findViewById(R.id.addressText);
        minimalPriceText = findViewById(R.id.minimalPriceText);
        forWhatText = findViewById(R.id.forWhatText);
        ratingText = findViewById(R.id.ratingText);
        ratingNameText = findViewById(R.id.ratingNameText);
        peculiarityText = findViewById(R.id.peculiarityText);
        descriptionText = findViewById(R.id.descriptionText);

        // TODO: make livedata models and observers later
        hotelImageView.setImageBitmap(DataLoader.getBitmapsMain().get(2));
        nameText.setText(DataLoader.getDataMain("name"));
        addressText.setText(DataLoader.getDataMain("adress"));
        minimalPriceText.setText(DataLoader.getDataMain("minimal_price"));
        forWhatText.setText(DataLoader.getDataMain("price_for_it"));
        ratingText.setText(DataLoader.getDataMain("rating"));
        ratingNameText.setText(DataLoader.getDataMain("rating_name"));
        descriptionText.setText(DataLoader.getDataMain("description"));

        peculiarityText.setText(String.join(", ", DataLoader.getArrayDataMain("peculiarities")));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isFinishing()) {
            DataLoader.setDirty();
        }
    }
}