package com.matcher.matcher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface tradeOrderRepository extends JpaRepository<TradeOrder, Long> {
    @Query(value = "SELECT * FROM AGGREGATE_ORDERS ORDER BY ID DESC", nativeQuery = true)
    ArrayList<TradeOrder> sortOrders();
}


