package com.sebastian.springcloud.msvc.items.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.springcloud.msvc.items.models.Item;
import com.sebastian.springcloud.msvc.items.services.ItemService;

@RestController
public class ItemController {

    private final ItemService itemService;



    public ItemController(@Qualifier("itemServiceWebClient") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> list(
        @RequestParam(name = "name", required = false) String name,
        @RequestHeader(name = "token-request", required = false) String tokenHeader
    ) {        

        System.out.println("Token Request Header: " + tokenHeader);
        System.out.println("Name Request Parameter: " + name);

        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Optional<Item> item = itemService.findById(id);

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity
            .status(404)
            .body(
                Collections.singletonMap("message", "Item not found")
            );
    }

}
