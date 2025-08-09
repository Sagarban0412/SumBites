package com.example.foodie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.example.foodie.database.MyDatabase;
import java.util.List;

public class TableActivity extends Activity {

    private MyDatabase db;
    private TableLayout tableLayout;
    private LinearLayout rootLayout, loadingOverlay;
    private ImageView loadingImage;

    private TextView loadingText;
    private final String loadingMessage = "SumBites...";
    private int loadingIndex = 0;
    private Handler typingHandler = new Handler();


    private final int[] columnsPerRow = {3, 2, 3, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        db = new MyDatabase(this);
        tableLayout = findViewById(R.id.tableLayout);
        rootLayout = findViewById(R.id.rootLayout);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingImage = findViewById(R.id.loadingImage);
        loadingText = findViewById(R.id.loadingText);

        // Load GIF
        Glide.with(this).asGif().load(R.drawable.loading).into(loadingImage);

        // Show loading overlay
        loadingOverlay.setVisibility(View.VISIBLE);

        startTypingAnimation();
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("Select Table Number");
        titleText.setTextSize(24);
        titleText.setTextColor(Color.WHITE);
        titleText.setTypeface(Typeface.DEFAULT_BOLD);
        titleText.setPadding(0, 0, 0, 24);
        titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rootLayout.addView(titleText, 0);

        // Initialize tables only once
        SharedPreferences prefs = getSharedPreferences("InitPrefs", MODE_PRIVATE);
        boolean initialized = prefs.getBoolean("table_initialized", false);
        if (!initialized) {
            initializeTables();
            prefs.edit().putBoolean("table_initialized", true).apply();
        }

        // Delay before showing content
        new Handler().postDelayed(() -> {
            loadingOverlay.setVisibility(View.GONE);
            loadTableButtonsDynamic();
        }, 2000);
    }



    //letter animation
    private void startTypingAnimation() {
        loadingIndex = 0;
        loadingText.setText("");
        typingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadingIndex < loadingMessage.length()) {
                    loadingText.append(String.valueOf(loadingMessage.charAt(loadingIndex)));
                    loadingIndex++;
                    typingHandler.postDelayed(this, 150); // delay between letters
                }
            }
        }, 150);
    }





    @Override
    protected void onResume() {
        super.onResume();
        CartManager.getInstance().clearCart();
        if (loadingOverlay.getVisibility() == View.GONE) {
            loadTableButtonsDynamic();
        }
    }

    private void loadTableButtonsDynamic() {
        tableLayout.removeAllViews();
        List<TableModel> tables = db.getAllTables();
        int tableIndex = 0;

        for (int cols : columnsPerRow) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            row.setPadding(0, 20, 0, 20);

            for (int col = 0; col < cols; col++) {
                if (tableIndex >= tables.size()) break;

                TableModel table = tables.get(tableIndex++);
                Button button = new Button(this);
                button.setText(table.getTableName());
                button.setTextSize(18);
                button.setTextColor(Color.WHITE);
                button.setAllCaps(false);
                button.setBackgroundResource(R.drawable.modern_table_button);
                button.setPadding(40, 70, 40, 70);

                int status = db.getOrderStatus(table.getTableNumber());
                if (status == 1) {
                    button.setBackgroundTintList(
                            getResources().getColorStateList(android.R.color.holo_red_dark)
                    );
                } else {
                    button.setBackgroundTintList(
                            getResources().getColorStateList(android.R.color.holo_blue_dark)
                    );
                }

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                );
                params.setMargins(10, 10, 10, 10);
                button.setLayoutParams(params);

                final int tableNumFinal = table.getTableNumber();
                final String tableNameFinal = table.getTableName();

                button.setOnClickListener(v -> {
                    Toast.makeText(this, "Selected " + tableNameFinal, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(TableActivity.this, MenuActivity.class);
                    in.putExtra("table_number", tableNumFinal);
                    in.putExtra("table_name", tableNameFinal);
                    startActivity(in);
                });

                button.setOnLongClickListener(v -> {
                    db.setOrderStatus(table.getTableNumber(), 0);
                    Toast.makeText(this, "Reset " + table.getTableName() + " status", Toast.LENGTH_SHORT).show();
                    loadTableButtonsDynamic();
                    return true;
                });

                row.addView(button);
            }
            tableLayout.addView(row);
        }
    }

    private void initializeTables() {
        for (int i = 1; i <= 7; i++) {
            db.insertTableIfNotExists(i, "Table " + i);
            db.initializeTableStatus(i);
        }
        db.insertTableIfNotExists(8, "VIP");
        db.initializeTableStatus(8);
    }
}
