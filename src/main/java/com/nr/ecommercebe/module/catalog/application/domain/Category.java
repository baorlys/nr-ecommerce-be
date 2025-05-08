package com.nr.ecommercebe.module.catalog.application.domain;

import com.nr.ecommercebe.module.media.application.domain.StorageType;
import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity(name = "categories")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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
    @ToString.Exclude
    Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Category> subCategories;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Product> products;

    @Enumerated(EnumType.STRING)
    StorageType storageType;

    public Category(String id) {
        this.id = id;
    }
}
