package com.matcher.matcher;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.constraints.*;

public class Order implements Comparable{
    @Size(min=1, max=20)
    @NotBlank
    private String account;
    @Min(1)
    private int price;
    @Min(1)
    private int quantity;
    @Pattern(regexp = "buy|sell")
    private String action;

    public Order(String account, int quantity, int price, String action) {
        this.account = account;
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
    public void setAccount(String account) {this.account = account;}
    public void setAction(String action) {this.action = action;}

    public String getAccount() {return account;}
    public int getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public String getAction() {return action;}

    public String toString(){
        String response = "Account: " + this.account
                + "\n Price: " + this.price
                + "\n Quantity: " + this.quantity
                + "\n Action: " + this.action;
        return response;
    }
    @Override
    public int compareTo(Object o) {
        int comparePrice = ((Order)o).price;
        return comparePrice-this.price;
    }
}
