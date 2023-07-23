package com.products.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientDTO {
    private String name;
    private Integer quantity;
    private Float price;
}
