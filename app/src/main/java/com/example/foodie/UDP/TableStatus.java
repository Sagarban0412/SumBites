package com.example.foodie.UDP;

public class TableStatus {
    public int tableId;
    public boolean isOccupied;

    public TableStatus(int tableId, boolean isOccupied) {
        this.tableId = tableId;
        this.isOccupied = isOccupied;
    }
}
