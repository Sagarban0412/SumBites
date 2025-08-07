package com.example.foodie.model;

public class TrData {
    public int quantity;
    public int p_id;

    public TrData(int quantity,int p_id){
        this.p_id=p_id;
        this.quantity=quantity;
    }

    public int getP_id() {
        return p_id;
    }

    public int getQty() {
        return quantity;
    }
}
