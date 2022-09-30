package com.matcher.matcher;

public class TradeOrder{
    private int price;
    private int quantity;
    private String buyer;
    private String seller;

    public TradeOrder(int quantity, int price, String buyer, String seller) {
        this.price = price;
        this.quantity = quantity;
        this.buyer = buyer;
        this.seller = seller;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setBuyer(String buyer) {this.buyer = buyer;}
    public void setSeller(String seller) {this.seller = seller;}

    public String getBuyer() {return buyer;}
    public int getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public String getSeller() {return seller;}

    public String toString(){
        String response = "Price: " + this.price
                + "\nQuantity: " + this.quantity
                + "\nBuyer: " + this.buyer
                + "\nSeller: " + this.seller;
        return response;
    }

}