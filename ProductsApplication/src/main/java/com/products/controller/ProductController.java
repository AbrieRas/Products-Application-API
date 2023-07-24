package com.products.controller;

import com.products.entities.Ingredient;
import com.products.entities.Product;
import com.products.models.*;
import com.products.repositories.IngredientRepository;
import com.products.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequestMapping(value = "/products")
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    public ProductController(final ProductRepository productRepository, final IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Product> getProducts() {
        return this.productRepository.findAll();
    }

    @GetMapping(value = "/search")
    public Iterable<Product> searchProduct(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "quantity", required = false) Integer quantity,
            @RequestParam(name = "price", required = false) Float price,
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "productReference", required = false) String productReference
    ) {
        List<Product> searchResults = fetchProducts(id, name, quantity, price, category, productReference);
        return searchResults;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ProductAddDTO> addProduct(@RequestBody ProductAddDTO productAddDTO) {

        /** Null check RequestBody fields
         * @param ProductAddDTO
         */
        nullCheckProductDTO(productAddDTO);

        String productReference = generateUniqueReference();

        Product productToSave = new Product();
        productToSave.setName(productAddDTO.getName());
        productToSave.setQuantity(productAddDTO.getQuantity());
        productToSave.setPrice(productAddDTO.getPrice());
        productToSave.setCategory(productAddDTO.getCategory());
        productToSave.setProductReference(productReference);
        Product savedProduct = this.productRepository.save(productToSave);

        IngredientAddDTO[] responseIngredients = new IngredientAddDTO[productAddDTO.getIngredients().length];
        int ingredientCounter = 0;

        for (IngredientAddDTO ingredientAddDTO : productAddDTO.getIngredients()) {
            Ingredient ingredientToSave = new Ingredient();
            ingredientToSave.setProductReference(productReference);
            ingredientToSave.setName(ingredientAddDTO.getName());
            ingredientToSave.setQuantity(ingredientAddDTO.getQuantity());
            ingredientToSave.setPrice(ingredientAddDTO.getPrice());
            Ingredient savedIngredient = this.ingredientRepository.save(ingredientToSave);

            responseIngredients[ingredientCounter] = new IngredientAddDTO(
                    savedIngredient.getId(),
                    savedIngredient.getProductReference(),
                    savedIngredient.getName(),
                    savedIngredient.getQuantity(),
                    savedIngredient.getPrice()
            );

            ingredientCounter++;
        }

        ProductAddDTO responseProduct = productAddDTO;
        responseProduct.setId(savedProduct.getId());
        responseProduct.setIngredients(responseIngredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseProduct);
    }

    @PutMapping(value = "/update/{id}")
    public Product updateProduct(@PathVariable(value = "id") Integer id, @RequestBody Product product) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id not found");
        }

        Product productToUpdate = productOptional.get();
        Product productToSave = updateProductParams(productToUpdate, product);
        System.out.println("productToSave.getId():" + productToSave.getId());
        Product savedProduct = this.productRepository.save(productToSave);
        return ResponseEntity.status(HttpStatus.OK).body(savedProduct).getBody();
    }

    @DeleteMapping(value = "/remove/{id}")
    @Transactional
    public Product deleteProduct(@PathVariable Integer id) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id not found");
        }

        Product productToDelete = productOptional.get();
        this.productRepository.delete(productToDelete);
        String productReference = productToDelete.getProductReference();
        this.ingredientRepository.deleteByProductReference(productReference);
        return ResponseEntity.status(HttpStatus.OK).body(productToDelete).getBody();
    }

    private void nullCheckProductDTO(ProductAddDTO productAddDTO) {
        if (productAddDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product cannot be null");
        }

        if (productAddDTO.getName() == null || productAddDTO.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty");
        }

        if (productAddDTO.getQuantity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity cannot be null");
        }

        if (productAddDTO.getPrice() == null || productAddDTO.getPrice().isNaN()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be null");
        }

        if (productAddDTO.getCategory() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product category cannot be null");
        }

        // Ingredient checks
        for (IngredientAddDTO ingredientAddDTO : productAddDTO.getIngredients()) {
            if (ingredientAddDTO.getName() == null || ingredientAddDTO.getName().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient name cannot be empty");
            }

            if (ingredientAddDTO.getQuantity() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient quantity cannot be null");
            }

            if (ingredientAddDTO.getPrice() == null || ingredientAddDTO.getPrice().isNaN()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient price cannot be null");
            }
        }
    }

    private String generateUniqueReference() {
        LocalDateTime now = LocalDateTime.now();
        int randomNum = new Random().nextInt(1000);
        String reference = "REF-" + now.toString() + "-" + randomNum;
        return reference;
    }

    private List<Product> fetchProducts(Integer id, String name, Integer quantity, Float price, Category category, String productReference) {
        Iterable<Product> productsIterable = this.productRepository.findAll();
        List<Product> productsList = new ArrayList<>();
        for (Product product : productsIterable) {
            productsList.add(product);
        }

        if (id != null) {
            productsList.removeIf(product -> !product.getId().equals(id));
        }

        if (name != null) {
            productsList.removeIf(product -> !product.getName().toLowerCase().contains(name.toLowerCase()));
        }

        if (quantity != null) {
            productsList.removeIf(product -> !product.getQuantity().equals(quantity));
        }

        if (price != null) {
            productsList.removeIf(product -> !product.getPrice().equals(price));
        }

        if (category != null) {
            productsList.removeIf(product -> !product.getCategory().equals(category));
        }

        if (productReference != null) {
            productsList.removeIf(product -> !product.getProductReference().toLowerCase().contains(productReference.toLowerCase()));
        }

        return productsList;
    }

    private Product updateProductParams(Product productToUpdate, Product userProduct) {
        if (userProduct.getName() != null) {
            productToUpdate.setName(userProduct.getName());
        }

        if (userProduct.getQuantity() != null) {
            productToUpdate.setQuantity(userProduct.getQuantity());
        }

        if (userProduct.getPrice() != null) {
            productToUpdate.setPrice(userProduct.getPrice());
        }

        if (userProduct.getCategory() != null) {
            productToUpdate.setCategory(userProduct.getCategory());
        }

        if (userProduct.getProductReference() != null) {
            productToUpdate.setProductReference(userProduct.getProductReference());
        }

        return productToUpdate;
    }
}
