package com.app.ecom.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import lombok.Data;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Data
public class CartItem {

    // Unique ID for each cart item
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who owns this cart item
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The product added to the cart
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    // The number of this product added to the cart
    private Integer quantity;

    // The price of the product when it was added to the cart
    private BigDecimal price;
    
    // The time when this cart item was created
    @CreationTimestamp
    private LocalDateTime createdAt;

    // The time when this cart item was last updated
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
