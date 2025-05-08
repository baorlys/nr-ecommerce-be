package com.nr.ecommercebe.module.review.application.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewFilter {
    String productId;
    String sortBy;
    String sortDirection;
    List<Integer> ratings;
}
