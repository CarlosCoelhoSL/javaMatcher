package com.matcher.matcher;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TradeOrderTests {

    OrderBook orderBook = new OrderBook();
    CCOrder order1 = new CCOrder(System.nanoTime(),"Wallace", 100, 10, "buy");
    CCOrder order2 = new CCOrder(System.nanoTime(),"Gromit", 100, 10, "sell");

    @Test
    void checkBasicTrade() {
        orderBook.match(order1);
        orderBook.match(order2);
        assertEquals(100, orderBook.tradeOrders.get(0).getQuantity());
    }
}
