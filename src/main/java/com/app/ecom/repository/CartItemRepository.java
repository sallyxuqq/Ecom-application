package com.app.ecom.repository;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

            // Find all cart items for a specific user
            List<CartItem> findByUser(User user);

            // Find an existing cart item for the same user and product
            CartItem findByUserAndProduct(User user, Product product);

            // Delete a cart item for a specific user and product
            void deleteByUserAndProduct(User user, Product product);

}