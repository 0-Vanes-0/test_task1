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
import java.io.Serializable;
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
    private static Map<String, List<Bitmap>> bitmapsMain = new HashMap<>();
    private static Map<Integer, RoomInfo> datasRoom = new HashMap<>();
    private static Map<String, String> datasReserve = new HashMap<>();
    // TODO: LiveData


    public static void load(Runnable post) {
        if (!initFlag) {
            ExecutorService urlExecutor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            urlExecutor.execute(() -> {
                JSONObject jsonObject1;
                String response1 = getUrlJson("https://run.mocky.io/v3/35e0d18e-2521-4f1b-a575-f0fe366f66e3");
                try {
                    assert response1 != null;
                    jsonObject1 = new JSONObject(response1);
                    datasMain.put("name", jsonObject1.getString("name"));
                    datasMain.put("adress", jsonObject1.getString("adress"));
                    datasMain.put("minimal_price", String.valueOf(jsonObject1.getInt("minimal_price")));
                    datasMain.put("price_for_it", jsonObject1.getString("price_for_it"));
                    datasMain.put("rating", String.valueOf(jsonObject1.getInt("rating")));
                    datasMain.put("rating_name", jsonObject1.getString("rating_name"));
                    datasMain.put("description", jsonObject1.getJSONObject("about_the_hotel").getString("description"));

                    JSONArray bitmapsUrls = jsonObject1.getJSONArray("image_urls");
                    List<Bitmap> bitmaps = new ArrayList<>();
                    for (int i = 0; i < bitmapsUrls.length(); i++) {
                        bitmaps.add(getUrlBitmap(bitmapsUrls.getString(i)));
                    }
                    bitmapsMain.put("image_urls", bitmaps);

                    JSONArray jsonArray = jsonObject1.getJSONObject("about_the_hotel").getJSONArray("peculiarities");
                    List<String> peculiarities = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        peculiarities.add(jsonArray.getString(i));
                    }
                    arrayDatasMain.put("peculiarities", peculiarities);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JSONObject jsonObject2;
                String response2 = getUrlJson("https://run.mocky.io/v3/f9a38183-6f95-43aa-853a-9c83cbb05ecd");
                try {
                    assert response2 != null;
                    jsonObject2 = new JSONObject(response2);
                    JSONArray jsonArray = jsonObject2.getJSONArray("rooms");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray peculiaritiesJSON = jsonArray.getJSONObject(i).getJSONArray("peculiarities");
                        JSONArray imagesJSON = jsonArray.getJSONObject(i).getJSONArray("image_urls");
                        List<String> peculiarities = new ArrayList<>();
                        List<Bitmap> images = new ArrayList<>();
                        for (int j = 0; j < peculiaritiesJSON.length(); j++) {
                            peculiarities.add(peculiaritiesJSON.getString(j));
                        }
                        for (int j = 0; j < imagesJSON.length(); j++) {
                            images.add(getUrlBitmap(imagesJSON.getString(j)));
                        }
                        datasRoom.put(jsonArray.getJSONObject(i).getInt("id"), new RoomInfo(
                                jsonArray.getJSONObject(i).getInt("id"),
                                jsonArray.getJSONObject(i).getString("name"),
                                jsonArray.getJSONObject(i).getInt("price"),
                                jsonArray.getJSONObject(i).getString("price_per"),
                                peculiarities,
                                images
                        ));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // TODO: other urls

                handler.post(() -> {
                    initFlag = true;
                    post.run();
                });
            });
        } else {
            post.run();
        }
    }

    public static void setDirty() {
        initFlag = false;
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

    public static String getDataMain(String key) {
        return datasMain.getOrDefault(key, "");
    }

    public static List<Bitmap> getBitmapsMain() {
        return bitmapsMain.get("image_urls");
    }

    public static List<String> getArrayDataMain(String key) {
        return arrayDatasMain.getOrDefault(key, new ArrayList<>());
    }

    public static Map<Integer, RoomInfo> getRoomsInfo() {
        return datasRoom == null ? new HashMap<>() : datasRoom;
    }
}

class RoomInfo implements Serializable {
    private Integer id;
    private String name;
    private Integer price;
    private String pricePer;
    private List<String> peculiarities;
    private List<Bitmap> images;

    public RoomInfo(Integer id, String name, Integer price, String pricePer, List<String> peculiarities, List<Bitmap> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pricePer = pricePer;
        this.peculiarities = peculiarities;
        this.images = images;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getPricePer() {
        return pricePer;
    }

    public List<String> getPeculiarities() {
        return peculiarities == null ? new ArrayList<>() : peculiarities;
    }

    public String getPeculiaritiesAsString() {
        return String.join(", ", peculiarities);
    }

    public List<Bitmap> getImages() {
        return images == null ? new ArrayList<>() : images;
    }
}

