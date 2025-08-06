package com.example.foodie;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<String> {
    Activity context;
    String[] name;
    int[] quantity;
    int[] rate;
    int[] total;

    public MyListAdapter(Activity context, String[] name, int[] quantity, int[] rate, int[] total) {
        super(context, R.layout.custom_list_cart, name);
        this.context = context;
        this.name = name;
        this.quantity = quantity;
        this.rate = rate;
        this.total = total;
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
}
