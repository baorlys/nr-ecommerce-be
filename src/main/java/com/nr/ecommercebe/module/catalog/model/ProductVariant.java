package com.nr.ecommercebe.module.catalog.model;

import com.nr.ecommercebe.module.catalog.model.enums.ProductVariantUnit;
import com.nr.ecommercebe.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity(name = "product_variants")
@Getter
@Setter
@ToString(exclude = {"product"})
@EqualsAndHashCode(callSuper = true, exclude = {"product"})
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariant extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Product product;

    @Column(nullable = false)
    String name;

    @Column(precision = 10, scale = 2, nullable = false)
    BigDecimal weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ProductVariantUnit unit;

    @Column(precision = 20, scale = 2, nullable = false)
    BigDecimal price;

    @Column(nullable = false, columnDefinition = "integer default 0")
    Integer stockQuantity;

}
