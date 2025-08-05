package com.example.foodie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class MenuItemActivity extends Activity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item);

        TextView des = findViewById(R.id.menu_item_reduce);
        TextView qty = findViewById(R.id.menu_item_count);
        TextView inc = findViewById(R.id.menu_item_add);

        // Parse initial quantity safely
        int[] inqty = {Integer.parseInt(qty.getText().toString())};

        des.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { // Ensure touch action is DOWN
                if (inqty[0] > 0) inqty[0]--; // Prevent negative values
                qty.setText(String.valueOf(inqty[0]));
            }
            return true;
        });

        inc.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                inqty[0]++;
                qty.setText(String.valueOf(inqty[0]));
            }
            return true;
        });
    }
}
