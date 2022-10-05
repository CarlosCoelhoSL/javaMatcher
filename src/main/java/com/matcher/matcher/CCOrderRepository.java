package com.matcher.matcher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CCOrderRepository extends JpaRepository<CCOrder, Long> {
    List<CCOrder> findByOrderByPriceDesc();

    @Query(value = "SELECT * FROM CC_ORDERS ORDER BY PRICE DESC", nativeQuery = true)
    ArrayList<CCOrder> sortOrders();

}


