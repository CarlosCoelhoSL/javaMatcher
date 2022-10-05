package com.matcher.matcher;

import com.matcher.matcher.accountAuthenticator.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
public class MatcherController {
    @Autowired
    OrderBook orderBook;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    CCOrderRepository ccOrderRepository;
    @Autowired
    aggregateOrderRepository aggregateOrderRepository;
    @Autowired
    tradeOrderRepository tradeOrderRepository;

    @RequestMapping
    public String helloWorld() {
        return "Hello World from Spring Boot";
    }

    @GetMapping("/orders")
    public List<CCOrder> orders() throws Exception {
        if (ccOrderRepository.findAll().size() == 0) {
            throw new Exception("No orders found");
        } else {return ccOrderRepository.findByOrderByPriceDesc();}
    }

    @PutMapping(value="/addOrder")
    public String addOrder(@Valid @RequestBody CCOrder order, @RequestHeader("Authorization") String bearerToken) throws Exception {
        String jwtToken = bearerToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if (username.equals(order.getAccount())) {
            CCOrder newOrder = new CCOrder(System.nanoTime(), username, order.getQuantity(), order.getPrice(), order.getAction());
            orderBook.match(newOrder);
            System.out.println(newOrder);
            //orderBook.aggregateOrders();
            return "Order added: "+ newOrder.toString();
        } else {
            throw new Exception("Account username and authentication token username are mismatched - order not added");
        }
    }

    @GetMapping("/aggregateOrders")
    public ArrayList<AggregateOrder> aggregateOrders() throws Exception {
        System.out.println(aggregateOrderRepository.findAll());
        orderBook.aggregateOrders();
        System.out.println("");
        System.out.println(aggregateOrderRepository.findAll());
        if (aggregateOrderRepository.findAll().size() == 0) {
            throw new Exception("No aggregate orders found");
        } else {return aggregateOrderRepository.sortOrders();}
    }

    @GetMapping("/tradeOrders")
    public List<TradeOrder> tradeOrders() throws Exception {
        if (tradeOrderRepository.findAll().size() == 0) {
            throw new Exception("No trade orders found");
        } else {return tradeOrderRepository.findAll();}
    }

    @GetMapping("/privateOrders")
    public ArrayList<CCOrder> privateOrders(@RequestHeader("Authorization") String bearerToken) throws Exception {
        if (ccOrderRepository.findAll().size() == 0) {
            throw new Exception("No orders found :(");
        } else {
            ArrayList<CCOrder> privateOrders = new ArrayList<CCOrder>();
            String jwtToken = bearerToken.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            for (CCOrder order : ccOrderRepository.findAll()) {
                if (order.getAccount().equals(username)){
                    privateOrders.add(order);
                }
            }
            return privateOrders;
        }
    }

}
