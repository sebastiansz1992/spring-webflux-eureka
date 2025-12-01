package com.sebastian.springcloud.msvc.items.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sebastian.libs.msvc.commons.entities.Product;
import com.sebastian.springcloud.msvc.items.models.Item;

@Service
public class ItemServiceWebClient implements ItemService {

    private final WebClient webClient;
    private final Random random = new Random();

    public ItemServiceWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<Item> findAll() {
        return webClient
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> {
                    int quantity = random.nextInt(10) + 1;
                    return new Item(product, quantity);
                })
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());

        // try {
            return Optional
                    .ofNullable(webClient
                            .get()
                            .uri("/{id}", pathVariables)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(Product.class)
                            .map(product -> {
                                int quantity = random.nextInt(10) + 1;
                                return new Item(product, quantity);
                            })
                            .block());
        // } catch (WebClientResponseException e) {
        //     return Optional.empty();
        // }

    }

    @Override
    public Product save(Product product) {
        return webClient
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    @Override
    public Product update(Product product, Long id) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());

        return webClient
                .put()
                .uri("/{id}", pathVariables)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    @Override
    public void delete(Long id) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());

        webClient
                .delete()
                .uri("/{id}", pathVariables)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    

}
