package com.app.ecom.service;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();

        updateProductFromRequest(product, productRequest);

        Product savedProduct = productRepository.save(product);

        return mapToProductResponse(savedProduct);
    }

    // Get all active products
    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    // Mark a product as inactive
    public boolean deleteProduct(Long id) {

        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }
    public Optional<ProductResponse> updateProduct(
        Long id,
        ProductRequest productRequest) {

    return productRepository.findById(id)
            .map(existingProduct -> {

                updateProductFromRequest(
                        existingProduct,
                        productRequest
                );

                Product savedProduct =
                        productRepository.save(existingProduct);

                return mapToProductResponse(savedProduct);
            });
}

    private void updateProductFromRequest(
            Product product,
            ProductRequest productRequest) {

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.isActive());

        return response;
    }

    public List<ProductResponse> searchProducts(String keyword) {
    return productRepository.searchProducts(keyword)
            .stream()
            .map(this::mapToProductResponse)
            .collect(Collectors.toList());
    }
}