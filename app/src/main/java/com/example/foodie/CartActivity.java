//package com.example.foodie;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.example.foodie.database.MyDatabase;
//import com.example.foodie.MenuItem;
//import com.example.foodie.CartManager;
//import com.example.foodie.JsonExporter;
//
//import java.util.List;
//
//public class CartActivity extends Activity {
//    private static final String CHANNEL_ID = "order_channel";
//
//    private LinearLayout cartContainer;
//    private TextView totalText;
//    private Button placeOrder;
//    private int tableNumber;
//    private MyDatabase myDatabase;
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        createNotificationChannel();
//
//        cartContainer = findViewById(R.id.cart_container);
//        totalText = findViewById(R.id.cart_total);
//        placeOrder = findViewById(R.id.place_order);
//
//        tableNumber = getIntent().getIntExtra("table_number", -1);
//        if (tableNumber == -1) {
//            Toast.makeText(this, "Invalid table number", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        myDatabase = new MyDatabase(this);
//        List<MenuItem> cartItems = CartManager.getInstance().getCartItems();
//
//        double total = 0;
//        for (MenuItem item : cartItems) {
//            // Display each item
//            TextView itemTextView = new TextView(this);
//            String itemText = item.getName() + " x " + item.getQuantity() + " - Rs. " + (item.getQuantity() * item.getPrice());
//            itemTextView.setText(itemText);
//            itemTextView.setTextSize(18);
//            cartContainer.addView(itemTextView);
//
//            total += item.getPrice() * item.getQuantity();
//        }
//
//        totalText.setText("Total: Rs. " + total + " (Table " + tableNumber + ")");
//
//        placeOrder.setOnClickListener(e -> {
//            if (cartItems.isEmpty()) {
//                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Save to database
//            for (MenuItem item : cartItems) {
//                // new DB method: insertItem(String name, int quantity, double price, int tableNo)
//                myDatabase.insertItem(item.getName(), item.getQuantity(), item.getPrice(), tableNumber);
//            }
//
//            // Clear cart
//            CartManager.getInstance().clearCart();
//
//            // Log JSON
//            String json = JsonExporter.getOrderItemsForTableAsJson(this, tableNumber);
//            Log.d("JSON_SUBMITTED", json);
//
//            Toast.makeText(this, "Order placed for table " + tableNumber, Toast.LENGTH_SHORT).show();
//            showOrderNotification();
//
//            finish(); // Go back to menu
//        });
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Order Notifications",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            channel.setDescription("Notification after placing order");
//
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            if (manager != null) {
//                manager.createNotificationChannel(channel);
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void showOrderNotification() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.img)
//                .setContentTitle("Order Placed")
//                .setContentText("Order placed for table " + tableNumber + ". Thank you!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat.from(this).notify(101, builder.build());
//    }
//}
package com.example.foodie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.foodie.database.MyDatabase;
import com.example.foodie.MenuItem;
import com.example.foodie.CartManager;
import com.example.foodie.JsonExporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartActivity extends Activity {
    private static final String CHANNEL_ID = "order_channel";

    private LinearLayout cartContainer;
    private TextView txt;
    private Button placeOrder;
    private int tableNumber;
    private MyDatabase myDatabase;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        createNotificationChannel();

        cartContainer = findViewById(R.id.cart_container);
        placeOrder = findViewById(R.id.place_order);
        txt = findViewById(R.id.table);
        ListView cartList = findViewById(R.id.cartList);

        tableNumber = getIntent().getIntExtra("table_number", -1);
        txt.setText("Table"+tableNumber);
//        Log.d("table_numberCart", "Received table: " + tableNumber);

        if (tableNumber == -1) {
            Toast.makeText(this, "Invalid table number", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        myDatabase = new MyDatabase(this);
        List<MenuItem> cartItems = CartManager.getInstance().getCartItems();

        ArrayList<String> namesList = new ArrayList<>();
        ArrayList<Integer> qtysList = new ArrayList<>();
        ArrayList<Integer> ratesList = new ArrayList<>();
        ArrayList<Integer> totalsList = new ArrayList<>();

        for (MenuItem item : cartItems) {
            TextView itemTextView = new TextView(this);

            String name = item.getName();
            int qty = item.getQuantity();
            int rate = (int) item.getPrice();
            int total = qty * rate;

            // Add to lists
            namesList.add(name);
            qtysList.add(qty);
            ratesList.add(rate);
            totalsList.add(total);

            cartContainer.addView(itemTextView);
        }

// Convert ArrayLists to arrays
        String[] names = namesList.toArray(new String[0]);
        int[] qtys = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            qtys = qtysList.stream().mapToInt(i -> i).toArray();
        }
        int[] rates = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            rates = ratesList.stream().mapToInt(i -> i).toArray();
        }
        int[] totals = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            totals = totalsList.stream().mapToInt(i -> i).toArray();
        }

        Log.d("names", "Names: " + Arrays.toString(names));

// Now pass to your adapter
        MyListAdapter adapter = new MyListAdapter(this, names, qtys, rates, totals);
        cartList.setAdapter(adapter);

        placeOrder.setOnClickListener(e -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to database
            for (MenuItem item : cartItems) {
                // new DB method: insertItem(String name, int quantity, double price, int tableNo)
                myDatabase.insertItem(item.getName(), item.getQuantity(), item.getPrice(), tableNumber);
            }

            // Clear cart
            CartManager.getInstance().clearCart();
            myDatabase.initializeTableStatus(tableNumber);

            myDatabase.setOrderStatus(tableNumber, 1); // Mark table as having order placed


            // Log JSON
            String json = JsonExporter.getOrderItemsForTableAsJson(this, tableNumber);
            Log.d("JSON_SUBMITTED", json);

            Toast.makeText(this, "Order placed for table " + tableNumber, Toast.LENGTH_SHORT).show();
            showOrderNotification();

            finish(); // Go back to menu
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Order Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notification after placing order");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showOrderNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.img)
                .setContentTitle("Order Placed")
                .setContentText("Order placed for table " + tableNumber + ". Thank you!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(101, builder.build());
    }
}
