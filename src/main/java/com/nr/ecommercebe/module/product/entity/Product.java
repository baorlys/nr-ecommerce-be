package com.nr.ecommercebe.module.product.entity;


import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<ProductImage> productImages;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Review> reviews;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @ToString.Exclude

    Set<ProductVariant> productVariants;

    Boolean isFeatured;
}
