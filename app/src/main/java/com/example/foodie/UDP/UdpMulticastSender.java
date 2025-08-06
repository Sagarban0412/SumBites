package com.example.foodie.UDP;

import android.content.Context;
import android.util.Log;

import com.example.foodie.database.MyDatabase;
import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UdpMulticastSender {

    private static final String MULTICAST_IP = "239.0.0.224"; // Reserved multicast IP
    private static final int PORT = 15002;

    public static void sendTableStatus(Context context) {
        new Thread(() -> {
            try {
                MyDatabase db = new MyDatabase(context);
                List<Integer> tableList = db.getTablesInfo(); // Get list of table IDs

                Gson gson = new Gson();
                String json = gson.toJson(tableList);
                byte[] data = json.getBytes(StandardCharsets.UTF_8);

                InetAddress group = InetAddress.getByName(MULTICAST_IP);
                DatagramPacket packet = new DatagramPacket(data, data.length, group, PORT);

                MulticastSocket socket = new MulticastSocket();
                socket.send(packet);
                socket.close();

                Log.d("UdpMulticastSender", "Table list: " + json);

            } catch (Exception e) {
                Log.e("UdpMulticastSender", "UDP Error: " + e.getMessage());
            }
        }).start();
    }
}
