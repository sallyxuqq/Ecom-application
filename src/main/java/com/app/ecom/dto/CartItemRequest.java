package com.app.ecom.dto;

import lombok.Data;

@Data
public class CartItemRequest {

    // The ID of the product the user wants to add to the cart
    private Long productId;

    // The quantity of the product to add
    private Integer quantity;
}