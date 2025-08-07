package com.example.foodie;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<MenuItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(MenuItem item) {
        for (MenuItem cartItem : cartItems) {
            if (cartItem.getP_id() == item.getP_id() && cartItem.getTableNumber() == item.getTableNumber()) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }

        cartItems.add(new MenuItem(item)); // copy constructor
    }

    public List<MenuItem> getCartItems() {
        return new ArrayList<>(cartItems); // to avoid modification outside
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotalPrice() {
        double total = 0;
        for (MenuItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
