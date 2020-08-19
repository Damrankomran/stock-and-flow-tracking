package com.damrankomran.poc_order_management;

import com.damrankomran.poc_order_management.controller.ApiController;
import com.damrankomran.poc_order_management.model.Order;
import com.damrankomran.poc_order_management.repo.OrderRepository;
import com.damrankomran.poc_order_management.service.OrderService;
import com.damrankomran.poc_order_management.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiControllerTest {

    @Mock
    private OrderRepository orderRepository;

    private ApiController apiController;

    @BeforeEach
    public void init(){
        OrderService orderService = new OrderServiceImpl(orderRepository);
        apiController = new ApiController(orderService);
    }

    //------------------------ Get All Orders ------------------------------
    @Test
    public void test_getAllOrders_returnsOrders(){

        Order order1 = new Order();
        order1.setOrderID(1L);
        Order order2 = new Order();
        order2.setOrderID(2L);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1,order2));

        ResponseEntity<?> responseEntity = apiController.getAllOrders();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void test_getAllOrders_returnsNotFound(){

        ResponseEntity<?> responseEntity = apiController.getAllOrders();

        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    //------------------------ Get Order ------------------------------
    @Test
    public void test_getOrder_validID_returnsOrder(){
        Long validID = 1L;
        Order order = new Order();
        order.setOrderID(validID);

        when(orderRepository.findById(validID)).thenReturn(java.util.Optional.of(order));

        ResponseEntity<?> responseEntity = apiController.getOrder(validID);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void test_getOrder_validID_returnsNotFound(){
        Long validID = 1L;
        ResponseEntity<?> responseEntity = apiController.getOrder(validID);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void test_getOrder_validIdIsNull_returnsException(){
        ResponseEntity<?> responseEntity = apiController.getOrder(null);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_getOrder_validIdIsZero_returnsException(){
        ResponseEntity<?> responseEntity = apiController.getOrder(0L);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_getOrder_validIdIsNegativeValue_returnsException(){
        ResponseEntity<?> responseEntity = apiController.getOrder(-1L);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    //------------------------ Save Order ------------------------------
    @Test
    public void test_saveOrder(){

        Order order = new Order();
        order.setCustomerID(1L);
        order.setProductID(5L);

        ResponseEntity<?> responseEntity = apiController.saveOrder(order,UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
    }

    @Test
    public void test_saveOrder_customerIdAndCatalogIdIsNull(){
        ResponseEntity<?> responseEntity = apiController.saveOrder(new Order(),UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }

    @Test
    public void test_saveOrder_customerIdOrCatalogIdIsZero(){
        Order order1 = new Order();
        order1.setCustomerID(0L);
        order1.setProductID(5L);

        Order order2 = new Order();
        order2.setCustomerID(1L);
        order2.setProductID(0L);

        ResponseEntity<?> responseEntity1 = apiController.saveOrder(order1,UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());

        ResponseEntity<?> responseEntity2 = apiController.saveOrder(order2,UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity2.getStatusCode());
    }

    @Test
    public void test_saveOrder_customerIdOrCatalogIdIsNegativeValue(){
        Order order1 = new Order();
        order1.setCustomerID(-1L);
        order1.setProductID(5L);

        Order order2 = new Order();
        order2.setCustomerID(1L);
        order2.setProductID(-5L);

        ResponseEntity<?> responseEntity1 = apiController.saveOrder(order1,UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());

        ResponseEntity<?> responseEntity2 = apiController.saveOrder(order2,UriComponentsBuilder.newInstance());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity2.getStatusCode());
    }

    //------------------------ Update Order ------------------------------
    @Test
    public void test_updateOrder_validID(){
        Long validID = 1L;

        Order currentOrder = new Order();
        currentOrder.setOrderID(validID);
        currentOrder.setProductID(3L);
        currentOrder.setCustomerID(10L);

        Order newOrder = new Order();
        newOrder.setProductID(2L);
        newOrder.setCustomerID(11L);

        when(orderRepository.findById(validID)).thenReturn(java.util.Optional.of(currentOrder));


        ResponseEntity<?> responseEntity = apiController.updateOrder(validID,newOrder);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void test_updateOrder_validID_returnsNotFound(){
        Long validID = 1L;

        Order newOrder = new Order();
        newOrder.setProductID(2L);
        newOrder.setCustomerID(11L);

        ResponseEntity<?> responseEntity = apiController.updateOrder(validID,newOrder);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void test_updateOrder_orderIdIsNullOrNegativeValueOrZero_returnsException(){
        Order newOrder = new Order();
        newOrder.setProductID(2L);
        newOrder.setCustomerID(11L);

        //validID -> null
        Long validID = null;
        ResponseEntity<?> responseEntity1 = apiController.updateOrder(validID,newOrder);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());

        //validID -> negativeValue
        validID = -1L;
        ResponseEntity<?> responseEntity2 = apiController.updateOrder(validID,newOrder);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity2.getStatusCode());

        //validID -> zero
        validID =  0L;
        ResponseEntity<?> responseEntity3 = apiController.updateOrder(validID,newOrder);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity3.getStatusCode());
    }

    @Test
    public void test_updateOrder_IdIsNegativeValueOrNullOrZero_returnsException(){

        Order order = new Order();

        order.setProductID(0L);
        order.setCustomerID(11L);
        ResponseEntity<?> responseEntity1 = apiController.updateOrder(1L,order); //catalogID -> 0L
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());

        order.setProductID(-2L);
        ResponseEntity<?> responseEntity2 = apiController.updateOrder(1L,order); //catalogID -> -2L
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity2.getStatusCode());

        order.setProductID(2L);
        order.setCustomerID(0L);
        ResponseEntity<?> responseEntity3 = apiController.updateOrder(1L,order); //customerID -> 0L
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity3.getStatusCode());

        order.setCustomerID(-11L);
        ResponseEntity<?> responseEntity4 = apiController.updateOrder(1L,order);  //customerID -> -11L
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity4.getStatusCode());

        order.setProductID(null);
        order.setCustomerID(11L);
        ResponseEntity<?> responseEntity5 = apiController.updateOrder(1L,order); //catalogID -> null
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity5.getStatusCode());

        order.setProductID(2L);
        order.setCustomerID(null);
        ResponseEntity<?> responseEntity6 = apiController.updateOrder(1L,order); //customerID -> null
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity6.getStatusCode());
    }

    @Test
    public void test_updateOrder_validateRequestBodySaveOrder(){

        Long validID = 1L;
        Order order = new Order();
        order.setOrderID(1L);
        order.setCustomerID(2L);
        order.setProductID(3L);

        //orderID -> not null
        ResponseEntity<?> responseEntity1 = apiController.updateOrder(validID,order);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());

        //updateDate -> not null
        order.setOrderID(null);
        order.setUpdateDate(new Date());
        ResponseEntity<?> responseEntity2 = apiController.updateOrder(validID,order);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity2.getStatusCode());

        //orderDate -> not null
        order.setUpdateDate(null);
        order.setOrderDate(new Date());
        ResponseEntity<?> responseEntity3 = apiController.updateOrder(validID,order);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity3.getStatusCode());
    }

    //------------------------ Delete Order ByID ------------------------------
    @Test
    public void test_deleteOrder_validID(){
        Long validID = 1L;
        when(orderRepository.findById(validID)).thenReturn(java.util.Optional.of(new Order()));
        ResponseEntity<?> responseEntity = apiController.deleteOrderByID(validID);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteOrder_validID_IsNotFound(){
        Long validID = 1L;
        ResponseEntity<?> responseEntity = apiController.deleteOrderByID(validID);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void test_deleteOrder_validIdIsNullOrNegativeValueOrZero_returnsException(){

        Long validID = null;

        ResponseEntity<?> responseEntity1 = apiController.deleteOrderByID(validID);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity1.getStatusCode());

        validID = 0L;
        ResponseEntity<?> responseEntity2 = apiController.deleteOrderByID(validID);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity2.getStatusCode());

        validID = -1L;
        ResponseEntity<?> responseEntity3 = apiController.deleteOrderByID(validID);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity3.getStatusCode());
    }

    //------------------------ Delete All Orders ------------------------------
    @Test
    public void test_deleteAllOrders(){
        ResponseEntity<?> responseEntity = apiController.deleteAllOrders();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
}
