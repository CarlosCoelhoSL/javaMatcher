package com.matcher.matcher;

public class AggregateOrder{
    private int price;
    private int quantity;
    private String action;

    public AggregateOrder(int quantity, int price, String action) {
        this.quantity = quantity;
        this.price = price;
        this.action = action;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setAction(String action) {this.action = action;}

    public int getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public String getAction() {return action;}

    public String toString(){
        String response = "Price: " + this.price
                + "\nQuantity: " + this.quantity
                + "\nAction: " + this.action;
        return response;
    }

}