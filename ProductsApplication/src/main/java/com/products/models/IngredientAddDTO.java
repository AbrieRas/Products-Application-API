package com.products.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientAddDTO {
    private Integer id;
    private String productReference;
    private String name;
    private Integer quantity;
    private Float price;
}
