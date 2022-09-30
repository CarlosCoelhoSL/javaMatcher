package com.matcher.matcher;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AggregateOrderTests {
    OrderBook orderBook = new OrderBook();

    Order order1 = new Order("Wallace", 100, 10, "buy");
    Order order2 = new Order("Gromit", 100, 10, "sell");
    Order order3 = new Order("Gromit", 100, 20, "sell");
    Order order4 = new Order("Gromit", 100, 30, "sell");

    @Test
    void checkBuyAggOrders() {
        orderBook.match(order1);
        orderBook.match(order1);
        assertEquals(200, orderBook.aggregateOrders.get(0).getQuantity());
        assertEquals(1,orderBook.aggregateOrders.size());
    }

    @Test
    void checkSellAggOrders() {
        orderBook.match(order2);
        orderBook.match(order2);
        assertEquals(200, orderBook.aggregateOrders.get(0).getQuantity());
        assertEquals(1,orderBook.aggregateOrders.size());
    }

    @Test
    void checkMultiBuyAgg() {
        orderBook.match(order2);
        orderBook.match(order3);
        orderBook.match(order4);
        assertEquals(30, orderBook.aggregateOrders.get(0).getPrice());
        assertEquals(3,orderBook.aggregateOrders.size());
    }
}
