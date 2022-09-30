package com.matcher.matcher;


import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

@Component
public class OrderBook {
    ArrayList<Order> orders;
    ArrayList<AggregateOrder> aggregateOrders;
    ArrayList<TradeOrder> tradeOrders;

    public OrderBook() {
        this.orders = new ArrayList<Order>();
        this.aggregateOrders = new ArrayList<AggregateOrder>();
        this.tradeOrders = new ArrayList<TradeOrder>();
    }

    private void addOrder(@Valid Order order) {
        if (order.getPrice() > 0 && order.getQuantity() > 0 &&
                (order.getAction().equals("buy") || order.getAction().equals("sell"))) {
                    this.orders.add(order);
        }
        Collections.sort(this.orders);
    }


    private void trade(Order oldOrder,Order newOrder){
        System.out.println("hi from trade");
        int tradeQuantity = Math.min(oldOrder.getQuantity(), newOrder.getQuantity());
        int tradePrice = Math.min(oldOrder.getPrice(), newOrder.getPrice());
        Order amendedOrder;
        int oldOrderLoc = this.orders.indexOf(oldOrder);
        if (oldOrder.getQuantity()==newOrder.getQuantity()) {
            this.orders.remove(oldOrder);
        } else if (tradeQuantity== oldOrder.getQuantity()) {
            amendedOrder = newOrder;
            amendedOrder.setQuantity(amendedOrder.getQuantity()-tradeQuantity);
            this.orders.remove(oldOrder);
            this.match(amendedOrder);
        } else {
            this.orders.get(oldOrderLoc).setQuantity(oldOrder.getQuantity() - tradeQuantity);
        }
        //Add trade order to trades array
        String buyer;
        String seller;
        System.out.println("Buyer: "+newOrder.getAccount());
        System.out.println("Seller: "+oldOrder.getAccount());
        if (oldOrder.getAction().equals("buy")) {
            buyer = oldOrder.getAccount();
            seller = newOrder.getAccount();
        } else {
            buyer = newOrder.getAccount();
            seller = oldOrder.getAccount();
        }
        TradeOrder trade = new TradeOrder(tradeQuantity,tradePrice,buyer,seller);
        this.tradeOrders.add(0, trade);

    }


    public void match(@Valid Order newOrder) {
        Collections.sort(this.orders);
        Boolean matchFound = false;
        for (Order oldOrder : this.orders) {
            if (!matchFound) {
                if (!(oldOrder.getAccount().equals(newOrder.getAccount()))) {
                    if (!(oldOrder.getAction().equals(newOrder.getAction()))) {
                        if ((oldOrder.getAction().equals("sell") && oldOrder.getPrice() <= newOrder.getPrice()) ||
                                (oldOrder.getAction().equals("buy") && oldOrder.getPrice() >= newOrder.getPrice())) {
                            System.out.println("trade found");
                            this.trade(oldOrder, newOrder);
                            matchFound = true;
                            if (this.orders.size() == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (!matchFound) {
            this.addOrder(newOrder);
        }
        Collections.sort(this.orders);
        this.aggregateOrders();
        System.out.println(this.orders);
    }

    public void aggregateOrders() {
        this.aggregateOrders.clear();
        for (Order order : this.orders) {
            Boolean aggExists = false;
            for (AggregateOrder aggOrder : this.aggregateOrders) {
                if ((order.getPrice()==aggOrder.getPrice())&&(order.getAction().equals(aggOrder.getAction()))){
                    aggOrder.setQuantity(aggOrder.getQuantity()+order.getQuantity());
                    aggExists = true;
                }
            }
            if (!aggExists) {this.aggregateOrders.add(new AggregateOrder(order.getQuantity(),order.getPrice(),order.getAction()));}
            this.aggregateOrders.sort(Comparator.comparing(AggregateOrder::getPrice));
            Collections.reverse(this.aggregateOrders);
        }
    }
}
