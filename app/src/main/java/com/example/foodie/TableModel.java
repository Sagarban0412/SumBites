package com.example.foodie;

public class TableModel {
    private int tableNumber;
    private String tableName;

    public TableModel(int tableNumber, String tableName) {
        this.tableNumber = tableNumber;
        this.tableName = tableName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getTableName() {
        return tableName;
    }
}
