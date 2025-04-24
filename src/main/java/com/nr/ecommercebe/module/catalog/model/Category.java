package com.nr.ecommercebe.module.catalog.model;

import com.nr.ecommercebe.common.model.StorageType;
import com.nr.ecommercebe.common.model.BaseEntity;
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
    @Column(unique = true, nullable = false)
    String name;

    @Column(unique = true, nullable = false)
    String slug;

    @ToString.Exclude
    String description;

    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Category> subCategories;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Product> products;

    @Enumerated(EnumType.STRING)
    StorageType storageType;
}
