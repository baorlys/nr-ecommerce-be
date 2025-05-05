package com.nr.ecommercebe.module.review.api;

import com.nr.ecommercebe.module.review.api.request.ReviewFilter;
import com.nr.ecommercebe.module.review.api.request.ReviewRequestDto;
import com.nr.ecommercebe.module.review.api.response.ReviewResponseDto;
import com.nr.ecommercebe.shared.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService extends BaseCrudService<String, ReviewRequestDto, ReviewResponseDto> {
    Page<ReviewResponseDto> getAll(ReviewFilter filter, Pageable pageable);

    boolean isEligibleForReview(String productId, String userId);
}
