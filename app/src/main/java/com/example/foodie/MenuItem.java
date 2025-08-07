package com.example.foodie;

public class MenuItem {
    private String name;
    private int p_id;

    private double price;
    private int quantity;
    private int tableNumber; // 👈 NEW field to track which table the item belongs to

    // Regular constructor
    public MenuItem(String name, double price, int p_id) {
        this.name = name;
        this.price = price;
        this.quantity = 0; // Default quantity
        this.tableNumber = -1; // Default invalid table
        this.p_id = p_id;
    }

    // Copy constructor
    public MenuItem(MenuItem other) {
        this.name = other.name;
        this.price = other.price;
        this.quantity = other.quantity;
        this.p_id = other.p_id;
        this.tableNumber = other.tableNumber;
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

    public int getP_id() {
        return p_id;
    }
}
