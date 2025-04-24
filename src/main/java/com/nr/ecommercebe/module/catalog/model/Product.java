package com.nr.ecommercebe.module.catalog.model;


import com.nr.ecommercebe.module.review.model.Review;
import com.nr.ecommercebe.common.model.BaseEntity;
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

    @Column(unique = true, nullable = false)
    String slug;

    @Lob
    @Column(columnDefinition = "TEXT")
    String description;

    String shortDescription;

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
