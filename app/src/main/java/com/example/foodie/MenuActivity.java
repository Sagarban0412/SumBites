package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodie.fragment.DrinkFragment;
import com.example.foodie.fragment.FoodFragment;
import com.example.foodie.fragment.SmokeFragment;
import com.example.foodie.fragment.SpecialFragment;

public class MenuActivity extends AppCompatActivity {

    private int tableNumber;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        TextView txt = findViewById(R.id.table);
        Intent in = getIntent();
        tableNumber = in.getIntExtra("table_number", -1);
        tableName = in.getStringExtra("table_name");
        txt.setText(tableName);

        if (tableName != null) {
            txt.setText(tableName);
        } else {
            txt.setText("Unknown Table");
        }


        // Load FoodFragment with table info
        loadFragment(new FoodFragment());

        findViewById(R.id.btn_food).setOnClickListener(v -> loadFragment(new FoodFragment()));
        findViewById(R.id.btn_drink).setOnClickListener(v -> loadFragment(new DrinkFragment()));
        findViewById(R.id.btn_smoke).setOnClickListener(v -> loadFragment(new SmokeFragment()));
        findViewById(R.id.btn_special).setOnClickListener(v -> loadFragment(new SpecialFragment()));

        Button viewcart = findViewById(R.id.view_cart_button);
        viewcart.setOnClickListener(e -> {
            Intent in1 = new Intent(MenuActivity.this, CartActivity.class);
            in1.putExtra("table_number", tableNumber);       // ✅ Make sure this line exists
            in1.putExtra("table_name", tableName);           // ✅ Optional: pass table name if needed
            startActivity(in1);
        });

    }

    private void loadFragment(Fragment fragment) {
        // Pass table number to fragment
        Bundle args = new Bundle();
        args.putInt("table_number", tableNumber);
        fragment.setArguments(args);
        CartManager.getInstance().clearCart();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

