package com.example.foodie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodie.UDP.UdpMulticastSender;
import com.example.foodie.database.MyDatabase;

import java.util.List;
public class TableActivity extends Activity {

    private MyDatabase db;
    private TableLayout tableLayout;

    // Define your pattern of columns per row
    private final int[] columnsPerRow = {3, 2, 3,1};  // total 8 tables (3+2+3)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        db = new MyDatabase(this);
        tableLayout = findViewById(R.id.tableLayout);
        // 👇 Add this TextView dynamically before populating table buttons
        LinearLayout rootLayout = findViewById(R.id.rootLayout);  // must match your XML root LinearLayout id

        TextView titleText = new TextView(this);
        titleText.setText("Select Table Number");
        titleText.setTextSize(24);
        titleText.setTextColor(getResources().getColor(R.color.teal_700));
        titleText.setPadding(0, 0, 0, 24);
        titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        rootLayout.addView(titleText, 0);  // Add at top (index 0)

        // Initialize and populate tables
        SharedPreferences prefs = getSharedPreferences("InitPrefs", MODE_PRIVATE);
        boolean initialized = prefs.getBoolean("table_initialized", false);
        if (!initialized) {
            initializeTables();
            prefs.edit().putBoolean("table_initialized", true).apply();
        }

        loadTableButtonsDynamic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TABLE", "onResume called");

        loadTableButtonsDynamic(); // 👈 this reloads button states/colors
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
                button.setPadding(40, 70, 40, 70);

                int status = db.getOrderStatus(table.getTableNumber());
                button.setBackgroundColor(status == 1 ? Color.RED : Color.CYAN);

                // Weight so buttons fill row equally
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
                    loadTableButtonsDynamic();  // Refresh UI
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

