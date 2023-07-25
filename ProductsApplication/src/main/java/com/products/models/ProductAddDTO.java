package com.products.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductAddDTO {
    private Integer id;
    private String name;
    private Integer quantity;
    private Float price;
    private Category category;
    private IngredientAddDTO[] ingredients;
    private String productReference;
}
