package com.nr.ecommercebe.module.cart.application.domain;

import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity(name = "cart_items")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"cart"})
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem extends BaseEntity {
    @Column(nullable = false)
    String variantId;

    @Column(nullable = false)
    String name;

    String imageUrl;

    @Column(nullable = false)
    int quantity;

    @Column(precision = 20, scale = 2, nullable = false)
    BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

}
