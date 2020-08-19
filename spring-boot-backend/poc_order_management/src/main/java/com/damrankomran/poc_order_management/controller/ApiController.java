package com.damrankomran.poc_order_management.controller;

import com.damrankomran.poc_order_management.model.Order;
import com.damrankomran.poc_order_management.service.OrderService;
import com.damrankomran.poc_order_management.util.CustomErrorType;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiController {

    private final OrderService orderService;

    //------------------------ Get All Orders ------------------------------
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ResponseEntity<?> getAllOrders(){
        log.info("Get All Orders");
        try{
            List<Order> orders = orderService.findAllOrders();

            if(orders.isEmpty()){
                log.error("Orders not found");
                return new ResponseEntity<>("Orders not found!", HttpStatus.OK);
            }

            log.info("Orders found successful!");
            return new ResponseEntity<>(orders,HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Get Order ------------------------------
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(@PathVariable("id") Long id){
        log.info("Get Orders");
        try{
            validateID(id);
            Optional<Order> order = orderService.findByID(id);

            if(!order.isPresent()){
                log.error("Order id {} not found",id);
                return new ResponseEntity<>("Order id"+id+" not found!", HttpStatus.NOT_FOUND);
            }
            log.info("Order id {} found successful",id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Save Order ------------------------------
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<?> saveOrder(@RequestBody Order order, UriComponentsBuilder ucBuilder){
        log.info("Create order -> "+order);
        try{
            log.info("validateID before order 1 -> "+order);
            validateID(order.getCustomerID());
            log.info("validateID after order 1-> "+order);
            validateID(order.getProductID());
            log.info("validateID before order2 -> "+order);

            validateRequestBodySaveOrder(order);
            log.info("validateRequestBodySaveOrder after order -> "+order);

            order.setOrderDate(new Date());
            order.setUpdateDate(new Date());
            log.info("saveOrder before order -> "+order);
            orderService.saveOrder(order);
            log.info("saveOrder after order -> "+order);
            log.info("Create Order successful {}", order);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/api/order/{id}").buildAndExpand(order.getOrderID()).toUri());
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Update Order ------------------------------
    @RequestMapping(value = "/order/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id, @RequestBody Order order){
        log.info("Update order id {}",id);
        try{
            validateID(id);
            validateID(order.getCustomerID());
            validateID(order.getProductID());
            validateRequestBodySaveOrder(order);

            Optional<Order> currentOrder = orderService.findByID(id);

            if(!currentOrder.isPresent()){
                log.error("Order id {} not found",id);
                return new ResponseEntity<>(new CustomErrorType("Order id "+id+" not found!"), HttpStatus.NOT_FOUND);
            }
            order.setUpdateDate(new Date());
            order.setOrderID(currentOrder.get().getOrderID());
            order.setOrderDate(currentOrder.get().getOrderDate());
            orderService.updateOrder(order);
            log.info("Order update successful order");
            log.info("Current order {}",currentOrder.get());
            log.info("Update order {}",order);
            return new ResponseEntity<>(order,HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Delete Order ------------------------------
    @RequestMapping(value = "/order/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrderByID(@PathVariable("id") Long id){
        log.info("Fetching & Deleting order with id {}",id);
        try{
            validateID(id);
            Optional<Order> order = orderService.findByID(id);

            if(!order.isPresent()){
                log.error("Order id {} not found!",id);
                return new ResponseEntity<>(new CustomErrorType("Order id "+id
                        +" not found!"), HttpStatus.NOT_FOUND);
            }

            log.info("Order id {} found successful!",id);
            orderService.deleteOrderByID(id);
            log.info("Order {} deleted!",order);

            return new ResponseEntity<>(order.get(),HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //------------------------ Delete All Orders ------------------------------
    @RequestMapping(value = "/order", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllOrders(){
        log.info("Deleting all orders");
        try {
            orderService.deleteAllOrders();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    // ------------------- Validate ID -----------------------------
    private void validateID(@NonNull Long id) {
        log.info("validateOrderID");
        if (Long.valueOf(0L).equals(id)) {
            throw new RuntimeException("ID can not be 0!");
        }
        if(id < 0){
            throw new RuntimeException("ID can not be negative value!");
        }
    }

    private void validateRequestBodySaveOrder(Order order){
        log.info("validateRequestBodySaveOrder");
        log.info("order "+order.getOrderID());
        Optional<Long> optionalOrderID = Optional.ofNullable(order.getOrderID());
        Optional<Date> optionalOrderDate = Optional.ofNullable(order.getOrderDate());
        Optional<Date> optionalOrderUpdateDate = Optional.ofNullable(order.getUpdateDate());

        if (optionalOrderID.isPresent()) {
            throw new RuntimeException("orderID is generated value. It cannot be sent within the Request Body.");
        }
        if (optionalOrderUpdateDate.isPresent()) {
            throw new RuntimeException("updateDate is generated value. It cannot be sent within the Request Body.");
        }
        if (optionalOrderDate.isPresent()) {
            throw new RuntimeException("orderDate is generated value. It cannot be sent within the Request Body.");
        }
    }
}
