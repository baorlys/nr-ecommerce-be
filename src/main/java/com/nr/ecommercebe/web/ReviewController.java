package com.nr.ecommercebe.web;

import com.nr.ecommercebe.module.review.api.request.ReviewRequestDto;
import com.nr.ecommercebe.shared.model.PagedResponseSuccess;
import com.nr.ecommercebe.module.review.api.request.ReviewFilter;
import com.nr.ecommercebe.module.review.api.response.ReviewResponseDto;
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

    @GetMapping("/eligibility")
    public ResponseEntity<Boolean> isEligibleForReview(
            @RequestParam String productId,
            @RequestParam String userId) {
        boolean isEligible = reviewService.isEligibleForReview(productId, userId);
        return ResponseEntity.ok(isEligible);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(@RequestBody ReviewRequestDto review) {
        ReviewResponseDto createdReview = reviewService.create(review);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> update( @PathVariable String id, @RequestBody ReviewRequestDto review) {
        ReviewResponseDto updatedReview = reviewService.update(id, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
