package com.products.entities;

import com.products.models.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PRICE")
    private float price;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "INGREDIENTS")
    private Ingredient[] ingredients;
}
