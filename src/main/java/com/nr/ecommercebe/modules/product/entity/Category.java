package com.nr.ecommercebe.modules.product.entity;

import com.nr.ecommercebe.shared.domain.enums.StorageType;
import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity(name = "categories")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {
    String name;

    @ToString.Exclude
    String description;

    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    Category parent;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Product> products;

    @Enumerated(EnumType.STRING)
    StorageType storageType;
}
