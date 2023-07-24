package com.products.controller;

import com.products.entities.Ingredient;
import com.products.entities.Product;
import com.products.repositories.IngredientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/ingredients")
@RestController
public class IngredientController {
    final IngredientRepository ingredientRepository;

    public IngredientController(final IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Ingredient> getIngredients() {
        return this.ingredientRepository.findAll();
    }

    @GetMapping(value = "/search")
    public Iterable<Ingredient> searchIngredient(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "productReference", required = false) String productReference,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "quantity", required = false) Integer quantity,
            @RequestParam(name = "price", required = false) Float price
    ) {
        List<Ingredient> searchResults = fetchIngredients(id, productReference, name, quantity, price);
        return searchResults;
    }

    @PostMapping(value = "/add")
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

        Ingredient savedIngredient = this.ingredientRepository.save(ingredientToSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient);
    }

    @PutMapping(value = "/update/{id}")
    public Ingredient updateIngredient(@PathVariable(value = "id") Integer id, @RequestBody Ingredient ingredient) {
        Optional<Ingredient> ingredientOptional = this.ingredientRepository.findById(id);
        if (!ingredientOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient id not found");
        }

        Ingredient ingredientToUpdate = ingredientOptional.get();
        Ingredient ingredientToSave = updateIngredientParams(ingredientToUpdate, ingredient);
        Ingredient savedIngredient = this.ingredientRepository.save(ingredientToSave);
        return ResponseEntity.status(HttpStatus.OK).body(savedIngredient).getBody();
    }

    @DeleteMapping(value = "/remove/{id}")
    @Transactional
    public Ingredient deleteIngredient(@PathVariable Integer id) {
        Optional<Ingredient> ingredientOptional = this.ingredientRepository.findById(id);
        if (!ingredientOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient id not found");
        }

        Ingredient ingredientToDelete = ingredientOptional.get();
        this.ingredientRepository.delete(ingredientToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(ingredientToDelete).getBody();
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

    private List<Ingredient> fetchIngredients(Integer id, String productReference, String name, Integer quantity, Float price) {
        Iterable<Ingredient> ingredientsIterable = this.ingredientRepository.findAll();
        List<Ingredient> ingredientsList = new ArrayList<>();
        for (Ingredient ingredient : ingredientsIterable) {
            ingredientsList.add(ingredient);
        }

        if (id != null) {
            ingredientsList.removeIf(ingredient -> !ingredient.getId().equals(id));
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

    private Ingredient updateIngredientParams(Ingredient ingredientToUpdate, Ingredient userIngredient) {
        if (userIngredient.getProductReference() != null) {
            ingredientToUpdate.setProductReference(userIngredient.getProductReference());
        }

        if (userIngredient.getName() != null) {
            ingredientToUpdate.setName(userIngredient.getName());
        }

        if (userIngredient.getQuantity() != null) {
            ingredientToUpdate.setQuantity(userIngredient.getQuantity());
        }

        if (userIngredient.getPrice() != null) {
            ingredientToUpdate.setPrice(userIngredient.getPrice());
        }

        return ingredientToUpdate;
    }
}
