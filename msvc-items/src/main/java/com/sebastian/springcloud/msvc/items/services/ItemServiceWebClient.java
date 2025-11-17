package com.sebastian.springcloud.msvc.items.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sebastian.springcloud.msvc.items.models.Item;
import com.sebastian.springcloud.msvc.items.models.Product;

@Service
public class ItemServiceWebClient implements ItemService {

    private final WebClient.Builder webClientBuilder;
    private final Random random = new Random();

    public ItemServiceWebClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public List<Item> findAll() {
        return webClientBuilder
                .build()
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
                    .ofNullable(webClientBuilder
                            .build()
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

}
