package com.nr.ecommercebe.web.customer;

import com.nr.ecommercebe.common.model.PagedResponseSuccess;
import com.nr.ecommercebe.module.review.api.ReviewFilter;
import com.nr.ecommercebe.module.review.api.ReviewResponseDto;
import com.nr.ecommercebe.module.review.api.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewController {
    ReviewService reviewService;

    @GetMapping
    public ResponseEntity<PagedResponseSuccess<ReviewResponseDto>> getAll(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @ModelAttribute ReviewFilter filter) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviewPage = reviewService.getAll(filter, pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Review fetched successfully",reviewPage));
    }
}
