package com.damrankomran.poc_order_management;

import com.damrankomran.poc_order_management.model.Order;
import com.damrankomran.poc_order_management.repo.OrderRepository;
import com.damrankomran.poc_order_management.service.OrderService;
import com.damrankomran.poc_order_management.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    public void init(){
        orderService = new OrderServiceImpl(orderRepository);
    }

    //---------------------------------- findByID ----------------------------------------
    @Test
    public void test_findByID_validID_returnsOrder(){

        Long validID = 1L;

        Order expected = new Order();
        expected.setOrderID(validID);

        when(orderRepository.findById(validID)).thenReturn(java.util.Optional.of(expected));

        Optional<Order> actual = orderService.findByID(validID);

        actual.ifPresent(order -> assertEquals(expected,actual.get()));

        verify(orderRepository,times(1)).findById(validID);
    }

    @Test
    public void test_findByID_validIdIsNull(){

        Exception exception = assertThrows(NullPointerException.class, ()->orderService.findByID(null));

        assertEquals(NullPointerException.class,exception.getClass());
    }

    //---------------------------------- find All Orders ----------------------------------------
    @Test
    public void test_findAllOrders_returnsOrders(){

        Order order1 = new Order();
        order1.setOrderID(1L);

        Order order2 = new Order();
        order2.setOrderID(2L);

        List<Order> excepted = Arrays.asList(order1,order2);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1,order2));

        List<Order> actual = orderService.findAllOrders();

        assertEquals(excepted,actual);
        verify(orderRepository,times(1)).findAll();
    }

    //---------------------------------- save Order ----------------------------------------
    @Test
    public void test_saveOrder(){

        Order order = new Order();
        order.setOrderID(1L);

        orderService.saveOrder(order);

        verify(orderRepository,times(1)).save(order);
    }

    //---------------------------------- update Order ----------------------------------------
    @Test
    public void test_updateOrder(){

        Order order = new Order();
        order.setOrderID(1L);

        orderService.updateOrder(order);

        verify(orderRepository,times(1)).save(order);
    }

    //---------------------------------- delete Order ByID  ----------------------------------------
    @Test
    public void test_deleteOrderByID_validID(){

        Long validID = 1L;
        orderService.deleteOrderByID(validID);

        verify(orderRepository,times(1)).deleteById(validID);
    }

    @Test
    public void test_deleteOrderByID_validIdIsNull(){

        Exception exception = assertThrows(NullPointerException.class,()->orderService.deleteOrderByID(null));

        assertEquals(NullPointerException.class,exception.getClass());
    }

    //---------------------------------- delete All Orders ----------------------------------------
    @Test
    public void test_deleteAllOrders(){

        orderService.deleteAllOrders();
        verify(orderRepository,times(1)).deleteAll();
    }
}
