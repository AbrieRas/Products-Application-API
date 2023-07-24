package com.products.repositories;

import com.products.entities.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {
    void deleteByProductReference(String productReference);
}
