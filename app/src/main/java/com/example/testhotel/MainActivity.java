package com.example.testhotel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

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

        // TODO: make livedata models later
        hotelImageView.setImageBitmap(DataLoader.getBitmapMain(2));

        nameText.setText(DataLoader.getDataMain("name"));
        addressText.setText(DataLoader.getDataMain("adress"));
        minimalPriceText.setText(DataLoader.getDataMain("minimal_price"));
        forWhatText.setText(DataLoader.getDataMain("price_for_it"));
        ratingText.setText(DataLoader.getDataMain("rating"));
        ratingNameText.setText(DataLoader.getDataMain("rating_name"));
        descriptionText.setText(DataLoader.getDataMain("description"));

        List<String> peculiarities = DataLoader.getArrayDataMain("peculiarities");
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < peculiarities.size(); i++) {
            buffer.append(peculiarities.get(i));
            if (i < peculiarities.size() - 1) {
                buffer.append(", ");
            }
        }
        peculiarityText.setText(buffer.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isFinishing()) {
            DataLoader.setDirty();
        }
    }
}