package com.app.ecom.service;
import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import com.app.ecom.model.User;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    // Used to find products
    private final ProductRepository productRepository;

    // Used to save and find cart items
    private final CartItemRepository cartItemRepository;

    // Used to find users
    private final UserRepository userRepository;

    // Adds a product to a user's cart
    public boolean addToCart(Long userId, CartItemRequest request) {

        // Look for the product
        Optional<Product> productOpt =
                productRepository.findById(request.getProductId());

        // Return false if the product does not exist
        if (productOpt.isEmpty()) {
            return false;
        }

        // Get the actual product object
        Product product = productOpt.get();

        // Return false if the requested quantity is greater than the available stock
        if (product.getStockQuantity() < request.getQuantity()) {
            return false;
        }



        // Look for the user
        Optional<User> userOpt =
                userRepository.findById(userId);

        // Return false if the user does not exist
        if (userOpt.isEmpty()) {
            return false;
        }

        // Get the actual user object
        User user = userOpt.get();
        // Check whether this product already exists in the user's cart
        CartItem existingCartItem =
        cartItemRepository.findByUserAndProduct(user, product);

        // If the product already exists in the cart
        if (existingCartItem != null) {

            // Update the quantity of the existing cart item
            existingCartItem.setQuantity(
                    existingCartItem.getQuantity() + request.getQuantity());
            
            // Update the total price based on the new quantity
            existingCartItem.setPrice(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(existingCartItem.getQuantity())
                    )
            );

            // Save the updated cart item
            cartItemRepository.save(existingCartItem);} 

        else {

            // Create a new cart item
            CartItem cartItem = new CartItem();

            // Set the user who owns this cart item
            cartItem.setUser(user);

            // Set the product added to the cart
            cartItem.setProduct(product);

            // Set the quantity of this product added to the cart
            cartItem.setQuantity(request.getQuantity());

            // Set the price of the product when it was added to the cart
            cartItem.setPrice(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(request.getQuantity())
                    )
            );          

            // Save the new cart item
            cartItemRepository.save(cartItem);          
        }
        return true;

    }

    @Transactional
    public boolean removeFromCart(Long userId, Long productId) {

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        Product product = productOpt.get();

        CartItem existingCartItem =
                cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem == null) {
            return false;
        }

        cartItemRepository.deleteByUserAndProduct(user, product);

        return true;
    }


    public List<CartItem> getCart(Long userId) {

    return userRepository.findById(userId)
            .map(cartItemRepository::findByUser)
            .orElseGet(List::of);
}
            
}