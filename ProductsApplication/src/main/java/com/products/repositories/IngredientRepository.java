package com.products.repositories;

import com.products.entities.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {
    void deleteByProductReference(String productReference);
}
