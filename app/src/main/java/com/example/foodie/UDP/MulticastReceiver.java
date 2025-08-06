package com.example.foodie.UDP;

import android.content.Context;
import android.util.Log;

import com.example.foodie.database.MyDatabase;
import com.example.foodie.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MulticastReceiver extends Thread {
    private static final String MULTICAST_IP = "239.0.0.222";
    private static final int PORT = 15000;
    private final Context context;

    public MulticastReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_IP);
            socket.joinGroup(group);

            byte[] buffer = new byte[8192];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

//                Log.d("MulticastReceiver", "Received JSON: " + json);
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    String name = obj.getString("Name");
                    double price = obj.getDouble("Price");
                    int Cid = obj.getInt("CategoryID");
                    String category;
                    if (Cid == 0) {
                        category = "All";

                    } else if (Cid == 1 || Cid == 2) {
                        category = "food";
                    } else if (Cid == 3) {
                        category = "drink";
                    } else if (Cid == 4) {
                        category = "special";
                    } else {
                        category = "smoke";
                    }
                    MyDatabase db = new MyDatabase(context);
//                    Log.d("Items", "run:"+name+" "+price+" "+ category);
                    // Call your insertItem method
                    db.remoteInsertItem(name, price, category);


                    Gson gson = new Gson();
                    Type productListType = new TypeToken<List<Product>>() {
                    }.getType();
                    List<Product> products = gson.fromJson(json, productListType);
                    Log.d("Table Status", "Gson" + gson);
                    // TODO: Save to SQLite or update UI
                }
            }

            } catch(Exception ex){
                Log.e("MulticastReceiver", "Error: " + ex.getMessage());
            }
        }
    }

