package com.example.testhotel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

public class DataLoader {

    private static boolean initFlag = false;
    private static Map<String, String> datasMain = new HashMap<>();
    private static Map<String, List<String>> arrayDatasMain = new HashMap<>();
    private static List<Bitmap> bitmapsMain = new ArrayList<>();


    public static void load(Runnable post) {
        if (!initFlag) {
            ExecutorService urlExecutor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            urlExecutor.execute(() -> {
                JSONObject jsonObject;
                String response = getUrlJson("https://run.mocky.io/v3/35e0d18e-2521-4f1b-a575-f0fe366f66e3");
                try {
                    assert response != null;
                    jsonObject = new JSONObject(response);
                    datasMain.put("name", jsonObject.getString("name"));
                    datasMain.put("adress", jsonObject.getString("adress"));
                    datasMain.put("minimal_price", String.valueOf(jsonObject.getInt("minimal_price")));
                    datasMain.put("price_for_it", jsonObject.getString("price_for_it"));
                    datasMain.put("rating", String.valueOf(jsonObject.getInt("rating")));
                    datasMain.put("rating_name", jsonObject.getString("rating_name"));
                    datasMain.put("description", jsonObject.getJSONObject("about_the_hotel").getString("description"));

                    JSONArray bitmapsUrls = jsonObject.getJSONArray("image_urls");
                    for (int i = 0; i < bitmapsUrls.length(); i++) {
                        bitmapsMain.add(getUrlBitmap(bitmapsUrls.getString(i)));
                    }
                    JSONArray jsonArray = jsonObject.getJSONObject("about_the_hotel").getJSONArray("peculiarities");
                    List<String> peculiarities = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        peculiarities.add(jsonArray.getString(i));
                    }
                    arrayDatasMain.put("peculiarities", peculiarities);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // TODO: other urls

                handler.post(() -> {
                    initFlag = true;
                    post.run();
                });
            });
        }
    }

    public static void setDirty() {
        initFlag = false;
    }

    public static String getDataMain(String key) {
        return datasMain.getOrDefault(key, "");
    }

    public static Bitmap getBitmapMain(int index) {
        return 0 <= index && index < bitmapsMain.size() ? bitmapsMain.get(index) : null;
    }

    public static List<String> getArrayDataMain(String key) {
        return arrayDatasMain.getOrDefault(key, new ArrayList<>());
    }

    private static String getUrlJson(String urlAddress) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
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

    private static Bitmap getUrlBitmap(String urlAddress) {
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

