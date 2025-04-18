package com.nr.ecommercebe.module.catalog.model;

import com.nr.ecommercebe.module.catalog.model.enums.ProductVariantUnit;
import com.nr.ecommercebe.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity(name = "product_variants")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariant extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Product product;

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
