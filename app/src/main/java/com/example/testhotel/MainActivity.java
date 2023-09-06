package com.example.testhotel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static boolean initFlag = false;
    private static Map<String, String> datas = new HashMap<>();
    private static List<Bitmap> bitmaps = new ArrayList<>();
    private static List<String> peculiarities = new ArrayList<>();

    private ImageView hotelImageView;
    private TextView nameText;
    private TextView addressText;
    private TextView minimalPriceText;
    private TextView forWhatText;
    private TextView ratingText;
    private TextView ratingNameText;
    private TextView peculiarityText;
    private TextView descriptionText;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

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

        if(!initFlag) {
            ExecutorService urlExecutor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            urlExecutor.execute(() -> {
                JSONObject jsonObject;
                String response = getUrlJson("https://run.mocky.io/v3/35e0d18e-2521-4f1b-a575-f0fe366f66e3");
                try {
                    assert response != null;
                    jsonObject = new JSONObject(response);
                    datas.put("name", jsonObject.getString("name"));
                    datas.put("adress", jsonObject.getString("adress"));
                    datas.put("minimal_price", String.valueOf(jsonObject.getInt("minimal_price")));
                    datas.put("price_for_it", jsonObject.getString("price_for_it"));
                    datas.put("rating", String.valueOf(jsonObject.getInt("rating")));
                    datas.put("rating_name", jsonObject.getString("rating_name"));
                    datas.put("description", jsonObject.getJSONObject("about_the_hotel").getString("description"));

                    JSONArray bitmapsUrls = jsonObject.getJSONArray("image_urls");
                    for(int i = 0; i < bitmapsUrls.length(); i++) {
                        bitmaps.add(getUrlBitmap(bitmapsUrls.getString(i)));
                    }
                    JSONArray jsonArray = jsonObject.getJSONObject("about_the_hotel").getJSONArray("peculiarities");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        peculiarities.add(jsonArray.getString(i));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                handler.post(() -> {
                    initFlag = true;
                    updateViews();
                });
            });
        } else {
            updateViews();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isFinishing()) {
            initFlag = false;
        }
    }

    private String getUrlJson(String urlAddress) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);

            }
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Bitmap getUrlBitmap(String urlAddress) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            return BitmapFactory.decodeStream(stream);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private void updateViews() {
        nameText.setText(datas.get("name"));
        addressText.setText(datas.get("adress"));
        minimalPriceText.setText(datas.get("minimal_price"));
        forWhatText.setText(datas.get("price_for_it"));
        ratingText.setText(datas.get("rating"));
        ratingNameText.setText(datas.get("rating_name"));
        descriptionText.setText(datas.get("description"));

        hotelImageView.setImageBitmap(bitmaps.get(2));

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < peculiarities.size(); i++) {
            buffer.append(peculiarities.get(i));
            if (i < peculiarities.size() - 1) {
                buffer.append(", ");
            }
        }
        peculiarityText.setText(buffer.toString());
    }
}