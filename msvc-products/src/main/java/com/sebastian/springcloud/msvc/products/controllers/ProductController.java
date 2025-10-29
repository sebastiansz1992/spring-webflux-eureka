package com.sebastian.springcloud.msvc.products.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.sebastian.springcloud.msvc.products.entities.Product;
import com.sebastian.springcloud.msvc.products.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController()
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) throws InterruptedException {

        if(id.equals(10L)) {
            throw new IllegalStateException("Product not found");
        }

        if(id.equals(7L)) {
            TimeUnit.SECONDS.sleep(3L);
        }

        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

}
