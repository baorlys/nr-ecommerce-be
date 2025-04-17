package com.nr.ecommercebe.module.review.api;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewFilter {
    String productId;
    String sortBy;
    String sortDirection;
}
