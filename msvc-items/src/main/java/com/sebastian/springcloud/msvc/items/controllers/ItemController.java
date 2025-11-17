package com.sebastian.springcloud.msvc.items.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.springcloud.msvc.items.models.Item;
import com.sebastian.springcloud.msvc.items.models.Product;
import com.sebastian.springcloud.msvc.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RefreshScope
@RestController
public class ItemController {

    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final Environment env;

    @Value("${configuracion.texto}")
    private String texto;

    public ItemController(@Qualifier("itemServiceWebClient") ItemService itemService,
            CircuitBreakerFactory circuitBreakerFactory, Environment env) {
        this.itemService = itemService;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.env = env;
    }

    @GetMapping("/fetch-configs")
    public ResponseEntity<?> getConfigs() {
        Map<String, String> json = new HashMap<>();
        json.put("texto", texto);
        json.put("port", env.getProperty("local.server.port"));

        logger.info("Config Text: {}", texto);
        logger.info("Port: {}", env.getProperty("local.server.port"));

        if (env.getActiveProfiles().length > 0 &&
                env.getActiveProfiles()[0].equals("dev")) {
            json.put("author.name", env.getProperty("configuracion.autor.nombre"));
            json.put("author.email", env.getProperty("configuracion.autor.email"));
        }

        return ResponseEntity.ok(json);
    }

    @GetMapping
    public List<Item> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestHeader(name = "token-request", required = false) String tokenHeader) {

        logger.info("Token Request Header: {}", tokenHeader);
        logger.info("Name Request Parameter: {}", name);

        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {

        Optional<Item> item = circuitBreakerFactory
                .create("items")
                .run(() -> itemService.findById(id),
                        e -> {
                            logger.error("Error occurred: {}", e.getMessage());
                            Product product = new Product();
                            product.setId(id);
                            product.setName("Camara Kia");
                            product.setPrice(500.0);
                            return Optional.of(new Item(product, 10));
                        });

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity
                .status(404)
                .body(
                        Collections.singletonMap("message", "Item not found"));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct")
    @GetMapping("/details/{id}")
    public ResponseEntity<?> detail2(@PathVariable Long id) {

        Optional<Item> item = itemService.findById(id);

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity
                .status(404)
                .body(
                        Collections.singletonMap("message", "Item not found"));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct2")
    @TimeLimiter(name = "items")
    @GetMapping("/details2/{id}")
    public CompletableFuture<?> detail3(@PathVariable Long id) {

        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> item = itemService.findById(id);

            if (item.isPresent()) {
                return CompletableFuture.completedFuture(ResponseEntity.ok(item.get()));
            }

            return CompletableFuture.completedFuture(
                    ResponseEntity.status(404)
                            .body(Collections.singletonMap("message", "Item not found")));
        });
    }

    public ResponseEntity<?> getFallBackMethodProduct(Long id, Throwable e) {

        logger.error("Error occurred in getFallBackMethodProduct: {}", e.getMessage());
        Product product = new Product();
        product.setId(id);
        product.setName("Camara Sony - Fallback");
        product.setPrice(700.0);
        Item item = new Item(product, 15);

        return ResponseEntity.ok(item);
    }

    public CompletableFuture<?> getFallBackMethodProduct2(Long id, Throwable e) {

        return CompletableFuture.supplyAsync(() -> {
            logger.error("Error occurred in getFallBackMethodProduct: {}", e.getMessage());
            Product product = new Product();
            product.setId(id);
            product.setName("Camara Sony - Fallback");
            product.setPrice(700.0);
            Item item = new Item(product, 15);

            return ResponseEntity.ok(item);
        });
    }

}
