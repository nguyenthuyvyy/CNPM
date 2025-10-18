package com.foodfast.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodfast.product_service.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
