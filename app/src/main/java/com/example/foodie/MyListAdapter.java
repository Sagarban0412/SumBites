package com.example.foodie;

//import static com.example.foodie.UDP.TDataSender.sendOrderData;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodie.model.TrData;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {
    Activity context;
    String[] name;
    int[] quantity;
    int[] rate;
    int[] total;
    int[] p_id;
    List<TrData>itemList;
    public MyListAdapter(Activity context, String[] name, int[] quantity, int[] rate, int[] total,int[] p_id) {
        super(context, R.layout.custom_list_cart, name);
        this.context = context;
        this.name = name;
        this.quantity = quantity;
        this.rate = rate;
        this.total = total;
        this.p_id = p_id;
//        this.itemList = new ArrayList<>();
//
//        Log.d("MyListAdapter", "Constructor - p_id.length: " + p_id.length);
//
//        for (int i = 0; i < p_id.length; i++) {
//            TrData data = new TrData(quantity[i], p_id[i]);
//            itemList.add(data);
//            Log.d("MyListAdapter", "Added item: p_id=" + data.getP_id() + ", qty=" + data.getQty());
//        }


    }

    @Override
    public View getView(int position, View view, ViewGroup vg) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_cart, null, true);

        TextView txtName = rowView.findViewById(R.id.name);
        TextView txtQty = rowView.findViewById(R.id.qty);
        TextView txtRate = rowView.findViewById(R.id.rate);
        TextView txtTotal = rowView.findViewById(R.id.total);


        txtName.setText(name[position]);
        txtQty.setText(String.valueOf(quantity[position]));
        txtRate.setText(String.valueOf(rate[position]));
        txtTotal.setText(String.valueOf(total[position]));
        return rowView;
    }
//    public void sendDataTo(){
//        sendOrderData(itemList);
//        Log.d("sending data", "Sent data: "+itemList);
//    }
}
