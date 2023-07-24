package com.products.entities;

import com.products.models.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private Float price;

    @Column(name = "CATEGORY")
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Column(name = "PRODUCT_REFERENCE")
    private String productReference;
}
