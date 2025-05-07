package com.nr.ecommercebe.module.review.application.domain;

import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.user.application.domain.User;
import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Entity(name = "reviews")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Product product;

    @Column(nullable = false)
    @Min(0)
    @Max(5)
    Integer rating;

    String comment;


}
