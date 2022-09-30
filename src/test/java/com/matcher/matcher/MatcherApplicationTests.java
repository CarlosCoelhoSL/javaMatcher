package com.matcher.matcher;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MatcherApplicationTests {

	OrderBook orderBook = new OrderBook();
	Order falseOrder = new Order("Wallace", 200, -10, "buy");
	Order order1 = new Order("Wallace", 100, 10, "buy");
	Order order2 = new Order("Gromit", 100, 10, "sell");
	Order order3 = new Order("Gromit",200,10, "sell");
	Order order4 = new Order("Wallace", 200, 10,"buy");
	Order order5 = new Order("Wallace", 300, 10, "buy");
	Order order6 = new Order("Wallace", 300, 50, "buy");
	Order order7 = new Order("Gromit", 400, 5, "sell");
	Order order8 = new Order("Gromit", 300, 50, "sell");
	Order order9 = new Order("Wallace", 400, 5, "buy");
	@Test
	void contextLoads() {
	}


	@Test
	void checkAddOrder() {
		orderBook.match(order1);
		assertEquals(100, orderBook.orders.get(0).getQuantity());
		assertEquals(10, orderBook.orders.get(0).getPrice());
	}

	@Test
	void checkBadOrder() {
		orderBook.match(falseOrder);
		orderBook.match(order1);
		assertEquals(1, orderBook.orders.size());
	}

	@Test
	void checkBasicMatch() {
		orderBook.match(order1);
		orderBook.match(order2);
		assertEquals(0, orderBook.orders.size());
	}
	@Test
	void checkOverflowSell() {
		orderBook.match(order1);
		orderBook.match(order3);
		assertEquals(100,orderBook.orders.get(0).getQuantity());
		assertEquals("sell",orderBook.orders.get(0).getAction());
	}

	@Test
	void checkOverflowBuy() {
		orderBook.match(order2);
		orderBook.match(order4);
		assertEquals(100,orderBook.orders.get(0).getQuantity());
		assertEquals("buy",orderBook.orders.get(0).getAction());
	}

	@Test
	void matchTwoOverflowBuy() {
		orderBook.match(order2);
		orderBook.match(order2);
		orderBook.match(order5);
		assertEquals(100,orderBook.orders.get(0).getQuantity());
		assertEquals("buy",orderBook.orders.get(0).getAction());
	}

	@Test
	void matchThreeBuy() {
		orderBook.match(order2);
		orderBook.match(order2);
		orderBook.match(order2);
		orderBook.match(order5);
		assertEquals(0, orderBook.orders.size());
	}

	@Test
	void buyPriceHigh() {
		orderBook.match(order2);
		orderBook.match(order2);
		orderBook.match(order6);
		assertEquals(1, orderBook.orders.size());
		assertEquals(50,orderBook.orders.get(0).getPrice());
	}

	@Test
	void sellPriceLow() {
		orderBook.match(order1);
		orderBook.match(order1);
		orderBook.match(order1);
		orderBook.match(order7);
		assertEquals(1, orderBook.orders.size());
		assertEquals(5,orderBook.orders.get(0).getPrice());
	}

	@Test
	void noMatchPriceDiff() {
		orderBook.match(order8);
		orderBook.match(order9);
		assertEquals(2, orderBook.orders.size());
	}


}
