package com.example.foodie;

public class MenuItem {
    private String name;
    private double price;
    private int quantity;
    private int tableNumber; // 👈 NEW field to track which table the item belongs to

    // Regular constructor
    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 0; // Default quantity
        this.tableNumber = -1; // Default invalid table
    }

    // Copy constructor
    public MenuItem(MenuItem item) {
        this.name = item.name;
        this.price = item.price;
        this.quantity = item.quantity;
        this.tableNumber = item.tableNumber;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
