package com.example.foodie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodie.UDP.MulticastReceiver;
import com.example.foodie.UDP.TableMulticastReceiver;
import com.example.foodie.UDP.UdpMulticastSender;
import com.example.foodie.database.MyDatabase;

public class LoginActivity extends Activity {
    EditText username, password;
    Button loginBtn;
MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        db = new MyDatabase(this);
        //db.mulaTableInsert();
        new TableMulticastReceiver(this).start();
        new MulticastReceiver(this).start();
        new UdpMulticastSender().sendTableStatus(this);

        loginBtn.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (db.validateWaiter(user, pass)) {
                // Save login session or go to main
                Intent intent = new Intent(this, TableActivity.class);
                intent.putExtra("waiter_name", user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
