package com.example.foodie;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<MenuItem> cartItems = new ArrayList<>();

    private CartManager() {
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(MenuItem item) {
        // Check if already in cart and update quantity instead
        for (MenuItem cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(new MenuItem(item)); // Clone or add directly
    }

    public List<MenuItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        for (MenuItem item : cartItems) {
            item.setQuantity(0); // Reset quantity
        }
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
