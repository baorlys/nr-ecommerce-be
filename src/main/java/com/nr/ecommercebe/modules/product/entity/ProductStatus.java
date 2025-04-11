package com.nr.ecommercebe.modules.product.entity;


import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "product_statuses")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStatus extends BaseEntity {
    @Column(unique = true, nullable = false)
    String name;
}
