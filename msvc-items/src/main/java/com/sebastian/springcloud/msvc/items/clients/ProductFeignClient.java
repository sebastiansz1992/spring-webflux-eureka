package com.sebastian.springcloud.msvc.items.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sebastian.libs.msvc.commons.entities.Product;

@FeignClient(name = "msvc-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    Product details(@PathVariable Long id);
    
    @PostMapping
    Product create(@RequestBody Product product);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);

    @PutMapping("/{id}")
    Product update(@RequestBody Product product, @PathVariable Long id);

}
