package com.matcher.matcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
@Entity
@Table(name= "ccOrders")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class CCOrder implements Comparable{
    @Id
    private Long id;
    @Size(min=1, max=20)
    @NotBlank
    private String account;
    @Min(1)
    private int quantity;
    @Min(1)
    private int price;
    @Pattern(regexp = "buy|sell")
    private String action;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setAccount(String account) {this.account = account;}
    public void setAction(String action) {this.action = action;}
    public void setId(Long id) {this.id = id;}

    public String getAccount() {return account;}
    public int getPrice() {return price;}
    public int getQuantity() {return quantity;}
    public String getAction() {return action;}
    public Long getId() {return id;}

    public String toString(){
        String response = "Order id: " + this.id
                + "\nAccount: " + this.account
                + "\n Price: " + this.price
                + "\n Quantity: " + this.quantity
                + "\n Action: " + this.action;
        return response;
    }
    @Override
    public int compareTo(Object o) {
        int comparePrice = ((CCOrder)o).price;
        return comparePrice-this.price;
    }
}
