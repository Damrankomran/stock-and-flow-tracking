package com.damrankomran.poc_order_management.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "ORDER_MANAGEMENT")
public class Order implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id")
    private Long orderID;

    @Column(name = "customer_id", nullable = false)
    private Long customerID;

    @Column(name = "product_id", nullable = false)
    private Long productID;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Integer price;

    @Temporal(value = TemporalType.DATE)
    private Date orderDate;

    @Temporal(value = TemporalType.DATE)
    private Date updateDate;
}
