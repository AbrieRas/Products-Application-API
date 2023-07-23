package com.products.controller;

import com.products.entities.Ingredient;
import com.products.entities.Product;
import com.products.models.Category;
import com.products.models.IngredientDTO;
import com.products.models.ProductDTO;
import com.products.repositories.IngredientRepository;
import com.products.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    public ProductController(final ProductRepository productRepository, final IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping(value = "/products")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Product> getProducts() {
        return this.productRepository.findAll();
    }

    @PostMapping(value = "/products/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {

        /** Null check RequestBody fields
         * @param ProductDTO
         */
        nullCheckProductDTO(productDTO);

        String productReference = generateUniqueReference();

        Product productToSave = new Product();
        productToSave.setName(productDTO.getName());
        productToSave.setQuantity(productDTO.getQuantity());
        productToSave.setPrice(productDTO.getPrice());
        productToSave.setCategory(productDTO.getCategory());
        productToSave.setProductReference(productReference);
        this.productRepository.save(productToSave);

        for (IngredientDTO ingredientDTO : productDTO.getIngredientDTOs()) {
            Ingredient ingredientToSave = new Ingredient();
            ingredientToSave.setProductReference(productReference);
            ingredientToSave.setName(ingredientDTO.getName());
            ingredientToSave.setQuantity(ingredientDTO.getQuantity());
            ingredientToSave.setPrice(ingredientDTO.getPrice());
            this.ingredientRepository.save(ingredientToSave);
        }

        ProductDTO productDTOSaved = productDTO;
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTOSaved);
    }

    @PutMapping(value = "/products/update/{id}")
    public Product updateProduct(@PathVariable(value = "id") Integer id, @RequestBody Product product) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product id not found");
        }

        Product productToUpdate = productOptional.get();
        if (product.getName() != null) {
            productToUpdate.setName(product.getName());
        }

        if (product.getQuantity() != null) {
            productToUpdate.setQuantity(product.getQuantity());
        }

        if (product.getPrice() != null) {
            productToUpdate.setPrice(product.getPrice());
        }

        if (product.getCategory() != null) {
            productToUpdate.setCategory(product.getCategory());
        }

        if (product.getProductReference() != null) {
            productToUpdate.setProductReference(product.getProductReference());
        }

        Product savedProduct = this.productRepository.save(productToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(savedProduct).getBody();
    }

    @PutMapping(value = "/products/search")
    public Iterable<Product> searchProduct(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "quantity", required = false) Integer quantity,
            @RequestParam(name = "price", required = false) Float price,
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "productReference", required = false) String productReference
    ) {
        List<Product> searchResults = fetchProducts(name, quantity, price, category, productReference);
        return searchResults;
    }

    @DeleteMapping(value = "/products/remove/{id}")
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

    @GetMapping(value = "/ingredients")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Ingredient> getIngredients() {
        return this.ingredientRepository.findAll();
    }

    @PostMapping(value = "/ingredients/add")
    public ResponseEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient) {

        /** Null check RequestBody fields
         * @param Ingredient
         */
        nullCheckIngredient(ingredient);

        Ingredient ingredientToSave = new Ingredient();
        ingredientToSave.setProductReference(ingredient.getProductReference());
        ingredientToSave.setName(ingredient.getName());
        ingredientToSave.setQuantity(ingredient.getQuantity());
        ingredientToSave.setPrice(ingredient.getPrice());

        Ingredient ingredientSaved = this.ingredientRepository.save(ingredientToSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientSaved);
    }

    @PutMapping(value = "/ingredients/search")
    public Iterable<Ingredient> searchIngredient(
            @RequestParam(name = "productReference", required = false) String productReference,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "quantity", required = false) Integer quantity,
            @RequestParam(name = "price", required = false) Float price
    ) {
        List<Ingredient> searchResults = fetchIngredients(productReference, name, quantity, price);
        return searchResults;
    }

    @PutMapping(value = "/ingredients/update/{id}")
    public Ingredient updateIngredient(@PathVariable(value = "id") Integer id, @RequestBody Ingredient ingredient) {
        Optional<Ingredient> ingredientOptional = this.ingredientRepository.findById(id);
        if (!ingredientOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient id not found");
        }

        Ingredient ingredientToUpdate = ingredientOptional.get();
        if (ingredient.getProductReference() != null) {
            ingredientToUpdate.setProductReference(ingredient.getProductReference());
        }

        if (ingredient.getName() != null) {
            ingredientToUpdate.setName(ingredient.getName());
        }

        if (ingredient.getQuantity() != null) {
            ingredientToUpdate.setQuantity(ingredient.getQuantity());
        }

        if (ingredient.getPrice() != null) {
            ingredientToUpdate.setPrice(ingredient.getPrice());
        }

        Ingredient savedIngredient = this.ingredientRepository.save(ingredientToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(savedIngredient).getBody();
    }

    private void nullCheckProductDTO(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product cannot be null");
        }

        if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name cannot be empty");
        }

        if (productDTO.getQuantity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity cannot be null");
        }

        if (productDTO.getPrice() == null || productDTO.getPrice().isNaN()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be null");
        }

        if (productDTO.getCategory() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product category cannot be null");
        }

        // Ingredient checks
        for (IngredientDTO ingredientDTO : productDTO.getIngredientDTOs()) {
            if (ingredientDTO.getName() == null || ingredientDTO.getName().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient name cannot be empty");
            }

            if (ingredientDTO.getQuantity() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient quantity cannot be null");
            }

            if (ingredientDTO.getPrice() == null || ingredientDTO.getPrice().isNaN()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient price cannot be null");
            }
        }
    }

    private void nullCheckIngredient(Ingredient ingredient) {
        if (ingredient.getProductReference() == null || ingredient.getProductReference().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient product reference cannot be empty");
        }

        if (ingredient.getName() == null || ingredient.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient name cannot be empty");
        }

        if (ingredient.getQuantity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient quantity cannot be null");
        }

        if (ingredient.getPrice() == null || ingredient.getPrice().isNaN()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient price cannot be null");
        }
    }

    private String generateUniqueReference() {
        LocalDateTime now = LocalDateTime.now();
        int randomNum = new Random().nextInt(1000);
        String reference = "REF-" + now.toString() + "-" + randomNum;
        return reference;
    }

    private List<Product> fetchProducts(String name, Integer quantity, Float price, Category category, String productReference) {
        Iterable<Product> productsIterable = this.productRepository.findAll();
        List<Product> productsList = new ArrayList<>();
        for (Product product : productsIterable) {
            productsList.add(product);
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

    private List<Ingredient> fetchIngredients(String productReference, String name, Integer quantity, Float price) {
        Iterable<Ingredient> ingredientsIterable = this.ingredientRepository.findAll();
        List<Ingredient> ingredientsList = new ArrayList<>();
        for (Ingredient ingredient : ingredientsIterable) {
            ingredientsList.add(ingredient);
        }

        if (productReference != null) {
            ingredientsList.removeIf(ingredient -> !ingredient.getProductReference().toLowerCase().contains(productReference.toLowerCase()));
        }

        if (name != null) {
            ingredientsList.removeIf(ingredient -> !ingredient.getName().toLowerCase().contains(name.toLowerCase()));
        }

        if (quantity != null) {
            ingredientsList.removeIf(ingredient -> !ingredient.getQuantity().equals(quantity));
        }

        if (price != null) {
            ingredientsList.removeIf(ingredient -> !ingredient.getPrice().equals(price));
        }

        return ingredientsList;
    }
}
