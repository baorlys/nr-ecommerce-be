package com.nr.ecommercebe.module.product.entity;


import com.nr.ecommercebe.shared.domain.enums.StorageType;
import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "product_images")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;

    @Column(nullable = false)
    String imageUrl;

    String altText;

    @Column(columnDefinition = "boolean default false")
    Boolean isPrimary;

    Integer sortOrder;

    @Enumerated(EnumType.STRING)
    StorageType storageType;

}
