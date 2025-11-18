package com.sebastian.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.sebastian.libs.msvc.commons.entities.Product;
import com.sebastian.springcloud.msvc.items.clients.ProductFeignClient;
import com.sebastian.springcloud.msvc.items.models.Item;

import feign.FeignException;

@Service
public class ItemServiceFeign implements ItemService {

    private final ProductFeignClient productFeignClient;
    private final Random random = new Random();

    public ItemServiceFeign(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @Override
    public List<Item> findAll() {
        return productFeignClient.findAll()
                .stream()
                .map(product -> {
                    int quantity = random.nextInt(10) + 1;
                    return new Item(product, quantity);
                })
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        int quantity = random.nextInt(10) + 1;

        try {
            Product product = productFeignClient.details(id);
            return Optional.of(new Item(product, quantity));
        } catch (FeignException e) {
            return Optional.empty();
        }        
    }

    @Override
    public Product save(Product product) {
        return productFeignClient.create(product);
    }

    @Override
    public Product update(Product product, Long id) {
        return productFeignClient.update(product, id);
    }

    @Override
    public void delete(Long id) {
        productFeignClient.delete(id);
    }

}
