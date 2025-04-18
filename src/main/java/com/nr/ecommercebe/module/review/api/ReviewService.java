package com.nr.ecommercebe.module.review.api;

import com.nr.ecommercebe.common.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService extends BaseCrudService<String, ReviewRequestDto, ReviewResponseDto> {
    Page<ReviewResponseDto> getAll(ReviewFilter filter, Pageable pageable);
}
