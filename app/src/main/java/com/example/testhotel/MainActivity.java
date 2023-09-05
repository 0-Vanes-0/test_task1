package com.example.testhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView hotelImageView = findViewById(R.id.imageView);
        TextView nameText = findViewById(R.id.nameText);
        TextView addressText = findViewById(R.id.addressText);
        TextView minimalPriceText = findViewById(R.id.minimalPriceText);
        TextView forWhatText = findViewById(R.id.forWhatText);
        TextView ratingText = findViewById(R.id.ratingText);
        TextView ratingNameText = findViewById(R.id.ratingNameText);
        TextView peculiarityText = findViewById(R.id.peculiarityText);
        TextView descriptionText = findViewById(R.id.descriptionText);

        ExecutorService urlExecutor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        urlExecutor.execute(() -> {
            JSONObject jsonObject;
            List<Bitmap> bitmaps = new ArrayList<>();
            String response = getUrlJson("https://run.mocky.io/v3/35e0d18e-2521-4f1b-a575-f0fe366f66e3");
            try {
                assert response != null;
                jsonObject = new JSONObject(response);
                JSONArray bitmapsUrls = jsonObject.getJSONArray("image_urls");
                for(int i = 0; i < bitmapsUrls.length(); i++) {
                    bitmaps.add(getUrlBitmap(bitmapsUrls.getString(i)));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> {
                try {
                    nameText.setText(jsonObject.getString("name"));
                    addressText.setText(jsonObject.getString("adress"));
                    minimalPriceText.setText(String.valueOf(jsonObject.getInt("minimal_price")));
                    forWhatText.setText(jsonObject.getString("price_for_it"));
                    ratingText.setText(String.valueOf(jsonObject.getInt("rating")));
                    ratingNameText.setText(jsonObject.getString("rating_name"));
                    descriptionText.setText(jsonObject.getJSONObject("about_the_hotel").getString("description"));

                    hotelImageView.setImageBitmap(bitmaps.get(2));

                    JSONArray peculiarities = jsonObject.getJSONObject("about_the_hotel").getJSONArray("peculiarities");
                    StringBuilder buffer = new StringBuilder();
                    for(int i = 0; i < peculiarities.length(); i++) {
                        buffer.append(peculiarities.getString(i));
                        if(i < peculiarities.length() - 1) {
                            buffer.append(", ");
                        }
                    }
                    peculiarityText.setText(buffer.toString());

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        });

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
}