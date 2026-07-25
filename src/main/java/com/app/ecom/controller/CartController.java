package com.app.ecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    // Used to handle cart business logic
    private final CartService cartService;

    // Adds a product to the user's cart
    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader(name = "X-User-Id") Long userId,
            @RequestBody CartItemRequest request) {

        // Try to add the product to the user's cart
    boolean added = cartService.addToCart(userId, request);

    // Return a bad request if the product, stock, or user is invalid
    if (!added) {
        return ResponseEntity.badRequest()
                .body("User not found, product not found, or product out of stock");
        }

        // Return 201 Created if the product was added successfully
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}