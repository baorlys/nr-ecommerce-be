package com.nr.ecommercebe.modules.product.entity;


import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "products")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Column(unique = true, nullable = false)
    String name;

    String description;

    @Column(precision = 20, scale = 2, nullable = false)
    BigDecimal price;

    @Column(nullable = false, columnDefinition = "integer default 0")
    Integer stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    Set<ProductImage> productImages;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    Set<Review> reviews;

    @ManyToOne(fetch = FetchType.LAZY)
    ProductStatus productStatus;

    Boolean isFeatured;
}
