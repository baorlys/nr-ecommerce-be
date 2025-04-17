package com.nr.ecommercebe.module.review.model;

import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.user.model.User;
import com.nr.ecommercebe.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity(name = "reviews")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
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
