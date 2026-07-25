package com.app.ecom.repository;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.User;
import com.app.ecom.model.Product;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

            // Find an existing cart item for the same user and product
            CartItem findByUserAndProduct(User user, Product product);

}