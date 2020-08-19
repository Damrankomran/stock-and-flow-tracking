package com.damrankomran.poc_order_management.service;

import com.damrankomran.poc_order_management.model.Order;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<Order> findByID(Long orderID);

    List<Order> findAllOrders();

    void saveOrder(Order order);

    void updateOrder(Order order);

    void deleteOrderByID(Long orderID);

    void deleteAllOrders();
}
