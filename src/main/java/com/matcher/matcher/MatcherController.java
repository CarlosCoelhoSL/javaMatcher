package com.matcher.matcher;

import com.matcher.matcher.accountAuthenticator.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;


@RestController
public class MatcherController {
    @Autowired
    OrderBook orderBook;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @RequestMapping
    public String helloWorld() {
        return "Hello World from Spring Boot";
    }

    @GetMapping("/orders")
    public ArrayList<Order> orders() {
        if (orderBook.orders.size() == 0) {
            return null;
        } else {return orderBook.orders;}
    }

    @PutMapping(value="/addOrder")
    public void addOrder(@Valid @RequestBody Order order, @RequestHeader("Authorization") String bearerToken) {
        String jwtToken = bearerToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if (username.equals(order.getAccount())) {
            Order newOrder = new Order(username, order.getQuantity(), order.getPrice(), order.getAction());
            orderBook.match(newOrder);
        } else {
            System.out.println("Account username and authentication token username are mismatched - order not added");
        }
    }

    @GetMapping("/aggregateOrders")
    public ArrayList<AggregateOrder> aggregateOrders() {
        orderBook.aggregateOrders();
        if (orderBook.orders.size() == 0) {
            return null;
        } else {return orderBook.aggregateOrders;}
    }

    @GetMapping("/tradeOrders")
    public ArrayList<TradeOrder> tradeOrders() {
        if (orderBook.tradeOrders.size() == 0) {
            return null;
        } else {return orderBook.tradeOrders;}
    }

    @GetMapping("/privateOrders")
    public ArrayList<Order> privateOrders(@RequestHeader("Authorization") String bearerToken) {
        if (orderBook.orders.size() == 0) {
            return null;
        } else {
            ArrayList<Order> privateOrders = new ArrayList<Order>();
            String jwtToken = bearerToken.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            for (Order order : orderBook.orders) {
                if (order.getAccount().equals(username)){
                    privateOrders.add(order);
                }
            }
            return privateOrders;
        }
    }

}
