package com.example.foodie.UDP;

import android.util.Log;

import com.example.foodie.model.OrderData;
import com.example.foodie.model.TrData;
import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TDataSender {

    private static final String MULTICAST_IP = "239.0.0.225"; // Reserved multicast IP
    private static final int PORT = 15005;

    // Now accepts a List<Integer> as a parameter
    public static void sendOrderData(int quan, int id, int tableno) {
        new Thread(() -> {
            try {
                Gson gson = new Gson();
                OrderData order = new OrderData(quan, id, tableno);
                String json = gson.toJson(order); // Convert order object to JSON
                byte[] data = json.getBytes(StandardCharsets.UTF_8);

                InetAddress group = InetAddress.getByName(MULTICAST_IP);
                DatagramPacket packet = new DatagramPacket(data, data.length, group, PORT);

                MulticastSocket socket = new MulticastSocket();
                socket.send(packet);
                socket.close();

                Log.d("UdpMulticastSender", "Order data sent: " + json);

            } catch (Exception e) {
                Log.e("UdpMulticastSender", "UDP Error: " + e.getMessage());
            }
        }).start();
    }

}


