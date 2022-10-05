package com.matcher.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "tradeOrders")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TradeOrder{
    @Id
    private Long id;
    private int price;
    private int quantity;
    private String buyer;
    private String seller;

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