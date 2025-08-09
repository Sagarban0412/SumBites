package com.example.foodie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<MenuItem> menuItems;
    private final Context context;
    private final int tableNumber;

    // Constructor
    public MenuAdapter(List<MenuItem> menuItems, Context context, int tableNumber) {
        this.menuItems = menuItems;
        this.context = context;
        this.tableNumber = tableNumber;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText("Rs. " + item.getPrice());
        holder.qtyTextView.setText(String.valueOf(item.getQuantity()));

        // Decrease quantity
        holder.reduceImageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && item.getQuantity() > 0) {
                item.setQuantity(item.getQuantity() - 1);
                holder.qtyTextView.setText(String.valueOf(item.getQuantity()));
            }
            return true;
        });

        // Increase quantity
        holder.addImageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                item.setQuantity(item.getQuantity() + 1);
                holder.qtyTextView.setText(String.valueOf(item.getQuantity()));
            }
            return true;
        });

        // Add to cart
        holder.addToCartButton.setOnClickListener(v -> {
            if (item.getQuantity() > 0) {
                item.setTableNumber(tableNumber); // tag with current table number
                CartManager.getInstance().addToCart(item);
                Toast.makeText(context, item.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView, qtyTextView;
        ImageView reduceImageView, addImageView;
        Button addToCartButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.menu_item_name);
            priceTextView = itemView.findViewById(R.id.menu_item_price);
            qtyTextView = itemView.findViewById(R.id.menu_item_count);
            reduceImageView = itemView.findViewById(R.id.menu_item_reduce);
            addImageView = itemView.findViewById(R.id.menu_item_add);
            addToCartButton = itemView.findViewById(R.id.menu_item_add_to_cart);
        }
    }
}
