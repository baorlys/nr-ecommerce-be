package com.nr.ecommercebe.module.review.service;

import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.catalog.repository.ProductRepository;
import com.nr.ecommercebe.module.user.model.User;
import com.nr.ecommercebe.module.user.repository.UserRepository;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import com.nr.ecommercebe.module.review.api.request.ReviewFilter;
import com.nr.ecommercebe.module.review.api.request.ReviewRequestDto;
import com.nr.ecommercebe.module.review.api.response.ReviewResponseDto;
import com.nr.ecommercebe.module.review.api.ReviewService;
import com.nr.ecommercebe.module.review.model.Review;
import com.nr.ecommercebe.module.review.repository.ReviewRepository;
import com.nr.ecommercebe.module.review.repository.specification.ReviewSpecsBuilder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    ProductRepository productRepository;
    UserRepository userRepository;

    ModelMapper mapper;

    @Override
    public ReviewResponseDto create(ReviewRequestDto request) {
        if (!productRepository.existsById(request.getProductId())) {
            throw new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }

        if (!userRepository.existsById(request.getUserId())) {
            throw new RecordNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        if (reviewRepository.existsByProductIdAndUserId(request.getProductId(), request.getUserId())) {
            throw new RecordNotFoundException(ErrorCode.REVIEW_ALREADY_EXISTS.getMessage());
        }

        Review createdReview = reviewRepository.save(
                Review.builder()
                    .product(new Product(request.getProductId()))
                    .user(new User(request.getUserId()))
                    .rating(request.getRating())
                    .comment(request.getComment())
                    .build());

        return mapper.map(createdReview, ReviewResponseDto.class);
    }

    @Override
    public ReviewResponseDto update(String id, ReviewRequestDto request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.REVIEW_NOT_FOUND.getMessage()));

        review.setComment(request.getComment());
        review.setRating(request.getRating());

        Review updatedReview = reviewRepository.save(review);
        return mapper.map(updatedReview, ReviewResponseDto.class);
    }

    @Override
    public void delete(String id) {
        if (!reviewRepository.existsById(id)) {
            throw new RecordNotFoundException(ErrorCode.REVIEW_NOT_FOUND.getMessage());
        }

        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewResponseDto getById(String id) {
        return null;
    }

    @Override
    public Page<ReviewResponseDto> getAll(ReviewFilter filter, Pageable pageable) {
        Specification<Review> specsBuilder = new ReviewSpecsBuilder()
                .withProductId(filter.getProductId())
                .withRatings(filter.getRatings())
                .withSortBy(filter.getSortBy(), filter.getSortDirection())
                .build();

        return reviewRepository.findBy(specsBuilder, query -> query.page(pageable))
                .map(review -> mapper.map(review, ReviewResponseDto.class));
    }

    @Override
    public boolean isEligibleForReview(String productId, String userId) {
        return !reviewRepository.existsByProductIdAndUserId(productId, userId);
    }
}
