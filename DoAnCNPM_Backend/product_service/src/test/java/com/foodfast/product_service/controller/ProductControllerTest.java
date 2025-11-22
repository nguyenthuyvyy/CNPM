package com.foodfast.product_service.controller;

import com.foodfast.product_service.model.Product;
import com.foodfast.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Unit Tests")
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Burger");
        testProduct.setPrice(new BigDecimal("5.99"));
        testProduct.setQuantity(new BigDecimal("100"));
    }

    @Test
    @DisplayName("Should get all products")
    void testGetAllProducts() {
        List<Product> expected = Arrays.asList(testProduct);
        when(productService.getAllProducts()).thenReturn(expected);

        List<Product> result = productController.getAllProducts();

        assertThat(result).hasSize(1).contains(testProduct);
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("Should get product by id success")
    void testGetProductByIdSuccess() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        ResponseEntity<Product> result = productController.getProductById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(testProduct);
    }

    @Test
    @DisplayName("Should return 404 for non-existent product")
    void testGetProductByIdNotFound() {
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Product> result = productController.getProductById(999L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should create product")
    void testCreateProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        Product result = productController.createProduct(testProduct);

        assertThat(result).isNotNull().isEqualTo(testProduct);
        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("Should update product")
    void testUpdateProduct() {
        when(productService.updateProduct(1L, testProduct)).thenReturn(testProduct);

        ResponseEntity<Product> result = productController.updateProduct(1L, testProduct);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(testProduct);
    }

    @Test
    @DisplayName("Should delete product")
    void testDeleteProduct() {
        ResponseEntity<Void> result = productController.deleteProduct(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(productService, times(1)).deleteProduct(1L);
    }
}
