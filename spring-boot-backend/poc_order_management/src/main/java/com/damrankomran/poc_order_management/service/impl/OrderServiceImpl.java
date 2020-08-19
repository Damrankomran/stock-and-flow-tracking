package com.damrankomran.poc_order_management.service.impl;

import com.damrankomran.poc_order_management.model.Order;
import com.damrankomran.poc_order_management.repo.OrderRepository;
import com.damrankomran.poc_order_management.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    final OrderRepository orderRepository;

    @Override
    public Optional<Order> findByID(@NonNull Long orderID) {
        return orderRepository.findById(orderID);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void updateOrder(Order order) {
        saveOrder(order);
    }

    @Override
    public void deleteOrderByID(@NonNull Long orderID) {
        orderRepository.deleteById(orderID);
    }

    @Override
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

}
