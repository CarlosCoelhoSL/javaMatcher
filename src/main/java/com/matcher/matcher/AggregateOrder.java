package com.matcher.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "aggregateOrders")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class AggregateOrder{
    @Id
    private Long id;
    private int price;
    private int quantity;
    private String action;

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
        String response = "Order id: " + this.id
                +" \nPrice: " + this.price
                + "\nQuantity: " + this.quantity
                + "\nAction: " + this.action;
        return response;
    }

}