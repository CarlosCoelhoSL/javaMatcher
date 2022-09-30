package com.matcher.matcher;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TradeOrderTests {

    OrderBook orderBook = new OrderBook();
    Order order1 = new Order("Wallace", 100, 10, "buy");
    Order order2 = new Order("Gromit", 100, 10, "sell");

    @Test
    void checkBasicTrade() {
        orderBook.match(order1);
        orderBook.match(order2);
        assertEquals(100, orderBook.tradeOrders.get(0).getQuantity());
    }
}
