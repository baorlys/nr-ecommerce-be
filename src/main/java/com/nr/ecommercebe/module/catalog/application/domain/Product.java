package com.nr.ecommercebe.module.catalog.application.domain;


import com.nr.ecommercebe.module.catalog.application.domain.enums.ProductStatus;
import com.nr.ecommercebe.module.review.application.domain.Review;
import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "products")
@Getter
@Setter
@ToString(exclude = {"variants", "images", "reviews"})
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, orphanRemoval = true)
    Set<ProductImage> images;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    Set<ProductVariant> variants;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, orphanRemoval = true)
    Set<Review> reviews;

    Boolean isFeatured;

    @Enumerated(EnumType.STRING)
    ProductStatus status;

    public Product(String id) {
        this.id = id;
    }
}
