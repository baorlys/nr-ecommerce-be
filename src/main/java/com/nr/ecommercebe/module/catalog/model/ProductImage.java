package com.nr.ecommercebe.module.catalog.model;


import com.nr.ecommercebe.module.media.StorageType;
import com.nr.ecommercebe.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "product_images")
@Getter
@Setter
@ToString(exclude = {"product"})
@EqualsAndHashCode(callSuper = true, exclude = {"product"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
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
