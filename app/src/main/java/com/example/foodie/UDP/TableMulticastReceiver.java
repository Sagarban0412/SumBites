package com.example.foodie.UDP;

import android.content.Context;
import android.util.Log;

import com.example.foodie.database.MyDatabase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TableMulticastReceiver extends Thread {

    private static final String RTMULTICAST_IP = "239.0.0.223";
    private static final int RTPORT = 15001;
    private final Context context;

    public TableMulticastReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket(RTPORT);
            InetAddress group = InetAddress.getByName(RTMULTICAST_IP);
            socket.joinGroup(group);

            byte[] buffer = new byte[8192];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                Log.d("JSONReceiver", "Received JSON: " + json);

                try {
                    JSONArray jsonArray = new JSONArray(json);
                    MyDatabase db = new MyDatabase(context);

                    Set<Integer> newTableIds = new HashSet<>();

                    // Parse array and collect new table IDs
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = Integer.parseInt(obj.getString("Id"));
                        String name = obj.getString("Name");
                        Boolean taken = obj.getBoolean("IsTaken");
                        newTableIds.add(id);

                        if (!db.isTableExists(id)) {
                            db.remoteTableInsert(name, id);
                            Log.d("TableInfo", "Inserted: " + name + ", ID: " + id+" Taken: "+taken);
                        } else {
                            db.updateTableName(id, name); // Optional: in case name changed
                            Log.d("TableInfo", "Updated if changed: " + name + ", ID: " + id+"taken :"+taken);
                        }
                        db.insertOccupied(id, taken);
                    }

                    // Now remove old entries not in the new set
                    db.deleteTablesNotIn(newTableIds);

                } catch (JSONException e) {
                    Log.e("JSONReceiver", "Invalid JSON: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            Log.e("JSONReceiver", "Socket error: " + e.getMessage());
        }
    }

}

