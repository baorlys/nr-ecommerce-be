package com.nr.ecommercebe.module.review.service;

import com.nr.ecommercebe.common.exception.ErrorCode;
import com.nr.ecommercebe.common.exception.RecordNotFoundException;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.review.api.ReviewFilter;
import com.nr.ecommercebe.module.review.api.ReviewRequestDto;
import com.nr.ecommercebe.module.review.api.ReviewResponseDto;
import com.nr.ecommercebe.module.review.api.ReviewService;
import com.nr.ecommercebe.module.review.model.Review;
import com.nr.ecommercebe.module.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;

    ModelMapper mapper;

    @Override
    public ReviewResponseDto create(ReviewRequestDto request) {
        Review createdReview = reviewRepository.save(mapper.map(request, Review.class));
        return mapper.map(createdReview, ReviewResponseDto.class);
    }

    @Override
    public ReviewResponseDto update(String id, ReviewRequestDto request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.REVIEW_NOT_FOUND.getMessage()));
        mapper.map(request, review);
        review.setId(id);
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
        return null;
    }
}
