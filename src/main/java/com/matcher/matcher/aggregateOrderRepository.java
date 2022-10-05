package com.matcher.matcher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface aggregateOrderRepository extends JpaRepository<AggregateOrder, Long> {
    @Query(value = "SELECT * FROM AGGREGATE_ORDERS ORDER BY PRICE DESC", nativeQuery = true)
    ArrayList<AggregateOrder> sortOrders();
}


