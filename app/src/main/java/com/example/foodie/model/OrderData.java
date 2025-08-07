package com.example.foodie.model;

public class OrderData {
    int quantity;
    int itemId;
    int tableNo;

    public OrderData(int quantity, int itemId, int tableNo) {
        this.quantity = quantity;
        this.itemId = itemId;
        this.tableNo = tableNo;
    }
}