package com.sebastian.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sebastian.libs.msvc.commons.entities.Product;


public interface ProductRepository extends CrudRepository<Product, Long> {

}
