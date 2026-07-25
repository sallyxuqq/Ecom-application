package com.app.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.ecom.model.Product;

import java.util.List;


@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {
    // Find all active products
    List<Product> findByActiveTrue();

    //search products by keyword in name, only active and in stock
    @Query("""
    SELECT p FROM Product p
    WHERE p.active = true
    AND p.stockQuantity > 0
    AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Product> searchProducts(@Param("keyword") String keyword);
}