package com.products.controller;

import com.products.entities.Product;
import com.products.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequestMapping(value = "/products")
@RestController
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Product> getProducts() {
        return this.productRepository.findAll();
    }

    @PostMapping(value = "/add")
    public Product addProduct(@RequestBody Product product) {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product cannot be null");
        }

        if (product.getName() == null || product.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty");
        }

        if (product.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be null");
        }

        Product savedProduct = this.productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct).getBody();
    }
}
