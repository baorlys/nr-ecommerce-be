package com.nr.ecommercebe.module.review.web;

import com.nr.ecommercebe.module.review.application.dto.request.ReviewRequestDto;
import com.nr.ecommercebe.module.review.application.dto.request.ReviewFilter;
import com.nr.ecommercebe.module.review.application.dto.response.ReviewResponseDto;
import com.nr.ecommercebe.module.review.application.service.ReviewService;
import com.nr.ecommercebe.shared.dto.PagedResponseSuccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Reviews", description = "APIs for managing product reviews")
public class ReviewController {

    ReviewService reviewService;

    @GetMapping
    @Operation(
            summary = "Get all reviews",
            description = "Retrieves a paginated list of reviews with optional filters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reviews fetched successfully")
            }
    )
    public ResponseEntity<PagedResponseSuccess<ReviewResponseDto>> getAll(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "5") int size,
            @Parameter(description = "Review filter options") @ModelAttribute ReviewFilter filter) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviewPage = reviewService.getAll(filter, pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Review fetched successfully", reviewPage));
    }

    @GetMapping("/eligibility")
    @Operation(
            summary = "Check review eligibility",
            description = "Check if a user is eligible to review a specific product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Eligibility check result")
            }
    )
    public ResponseEntity<Boolean> isEligibleForReview(
            @Parameter(description = "Product ID") @RequestParam String productId,
            @Parameter(description = "User ID") @RequestParam String userId) {

        boolean isEligible = reviewService.isEligibleForReview(productId, userId);
        return ResponseEntity.ok(isEligible);
    }

    @PostMapping
    @Operation(
            summary = "Create review",
            description = "Creates a new review for a product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<ReviewResponseDto> create(
            @Valid @RequestBody ReviewRequestDto review) {

        ReviewResponseDto createdReview = reviewService.create(review);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update review",
            description = "Updates an existing review by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    public ResponseEntity<ReviewResponseDto> update(
            @Parameter(description = "Review ID") @PathVariable String id,
            @Valid @RequestBody ReviewRequestDto review) {

        ReviewResponseDto updatedReview = reviewService.update(id, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete review",
            description = "Deletes a review by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Review ID") @PathVariable String id) {

        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
