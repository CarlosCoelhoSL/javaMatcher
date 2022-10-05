package com.matcher.matcher;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.*;

@Component
public class OrderBook {
    ArrayList<CCOrder> orders;
    ArrayList<AggregateOrder> aggregateOrders;
    ArrayList<TradeOrder> tradeOrders;
    @Autowired
    private CCOrderRepository ccOrderRepository;
    @Autowired
    private aggregateOrderRepository aggregateOrderRepository;
    @Autowired
    private tradeOrderRepository tradeOrderRepository;


    public void addOrder(@Valid CCOrder order) {
        if (order.getPrice() > 0 && order.getQuantity() > 0 &&
                (order.getAction().equals("buy") || order.getAction().equals("sell"))) {
                    ccOrderRepository.save(order);
        }
    }


    private void trade(CCOrder oldOrder, CCOrder newOrder){
        System.out.println("hi from trade");
        int tradeQuantity = Math.min(oldOrder.getQuantity(), newOrder.getQuantity());
        int tradePrice = Math.min(oldOrder.getPrice(), newOrder.getPrice());
        CCOrder amendedOrder;
        if (oldOrder.getQuantity()==newOrder.getQuantity()) {
            ccOrderRepository.delete(oldOrder);
        } else if (tradeQuantity== oldOrder.getQuantity()) {
            amendedOrder = newOrder;
            amendedOrder.setQuantity(amendedOrder.getQuantity()-tradeQuantity);
            ccOrderRepository.delete(oldOrder);
            this.match(amendedOrder);
        } else {
            Optional<CCOrder> order = ccOrderRepository.findById(oldOrder.getId());
            order.get().setQuantity(order.get().getQuantity() - tradeQuantity);
        }
        //Add trade order to trades array
        String buyer;
        String seller;
        if (oldOrder.getAction().equals("buy")) {
            buyer = oldOrder.getAccount();
            seller = newOrder.getAccount();
        } else {
            buyer = newOrder.getAccount();
            seller = oldOrder.getAccount();
        }
        TradeOrder trade = new TradeOrder(System.nanoTime(),tradePrice,tradeQuantity,buyer,seller);
        tradeOrderRepository.save(trade);
    }


    public void match(@Valid CCOrder newOrder) {
        ArrayList<CCOrder> orderList = ccOrderRepository.sortOrders();
        Boolean matchFound = false;
        for ( CCOrder oldOrder : ccOrderRepository.findByOrderByPriceDesc()){
            if (!matchFound) {
                if (!(oldOrder.getAccount().equals(newOrder.getAccount()))) {
                    if (!(oldOrder.getAction().equals(newOrder.getAction()))) {
                        if ((oldOrder.getAction().equals("sell") && oldOrder.getPrice() <= newOrder.getPrice()) ||
                                (oldOrder.getAction().equals("buy") && oldOrder.getPrice() >= newOrder.getPrice())) {
                            System.out.println("trade found");
                            this.trade(oldOrder, newOrder);
                            matchFound = true;
                            if (ccOrderRepository.findAll().size() == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (!matchFound) {
            ccOrderRepository.save(newOrder);
        }
        //this.aggregateOrders();
        //System.out.println(aggregateOrderRepository.findAll());
    }

    public void aggregateOrders() {
        System.out.println("hi from aggregate order");
        aggregateOrderRepository.deleteAll();
        for (CCOrder order : ccOrderRepository.sortOrders()) {
            Boolean aggExists = false;
            for (AggregateOrder aggOrder : aggregateOrderRepository.findAll()) {
                if ((order.getPrice()==aggOrder.getPrice())&&(order.getAction().equals(aggOrder.getAction()))){
                    Optional<AggregateOrder> orderToChange = aggregateOrderRepository.findById(aggOrder.getId());
                    orderToChange.get().setQuantity(aggOrder.getQuantity()+order.getQuantity());
                    aggExists = true;
                }
            }
            if (!aggExists) {aggregateOrderRepository.save((new AggregateOrder(System.nanoTime(),order.getPrice(),order.getQuantity(),order.getAction())));}
        }
    }
}
