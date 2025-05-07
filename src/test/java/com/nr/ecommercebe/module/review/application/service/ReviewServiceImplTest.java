package com.nr.ecommercebe.module.review.application.service;

import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductRepository;
import com.nr.ecommercebe.module.review.application.domain.Review;
import com.nr.ecommercebe.module.review.application.dto.request.ReviewFilter;
import com.nr.ecommercebe.module.review.application.dto.request.ReviewRequestDto;
import com.nr.ecommercebe.module.review.application.dto.response.ReviewResponseDto;
import com.nr.ecommercebe.module.review.application.dto.response.UserName;
import com.nr.ecommercebe.module.review.infrastructure.repository.ReviewRepository;
import com.nr.ecommercebe.module.user.application.domain.User;
import com.nr.ecommercebe.module.user.infrastructure.repository.UserRepository;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewRequestDto reviewRequestDto;
    private Review review;
    private ReviewResponseDto reviewResponseDto;
    private ReviewFilter reviewFilter;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        reviewRequestDto = ReviewRequestDto.builder()
                .productId("prod1")
                .userId("user1")
                .rating(5)
                .comment("Great product!")
                .build();

        review = Review.builder()
                .id("rev1")
                .product(new Product("prod1"))
                .user(new User("user1"))
                .rating(5)
                .comment("Great product!")
                .build();

        reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setId("rev1");
        reviewResponseDto.setProductId("prod1");
        reviewResponseDto.setUser(new UserName("user1", "John","Doe"));
        reviewResponseDto.setRating(5);
        reviewResponseDto.setComment("Great product!");

        reviewFilter = new ReviewFilter();
        reviewFilter.setProductId("prod1");
        reviewFilter.setRatings(List.of(5));
        reviewFilter.setSortBy("rating");
        reviewFilter.setSortDirection("ASC");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void create_success_returnsReviewResponseDto() {
        // Arrange
        when(productRepository.existsById("prod1")).thenReturn(true);
        when(userRepository.existsById("user1")).thenReturn(true);
        when(reviewRepository.existsByProductIdAndUserId("prod1", "user1")).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(mapper.map(review, ReviewResponseDto.class)).thenReturn(reviewResponseDto);

        // Act
        ReviewResponseDto result = reviewService.create(reviewRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("rev1", result.getId());
        assertEquals("prod1", result.getProductId());
        assertEquals("user1", result.getUser().getId());
        assertEquals(5, result.getRating());
        assertEquals("Great product!", result.getComment());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(mapper, times(1)).map(review, ReviewResponseDto.class);
    }

    @Test
    void create_productNotFound_throwsRecordNotFoundException() {
        // Arrange
        when(productRepository.existsById("prod1")).thenReturn(false);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> reviewService.create(reviewRequestDto));
        assertEquals("Product not found", exception.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void create_userNotFound_throwsRecordNotFoundException() {
        // Arrange
        when(productRepository.existsById("prod1")).thenReturn(true);
        when(userRepository.existsById("user1")).thenReturn(false);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> reviewService.create(reviewRequestDto));
        assertEquals("User not found", exception.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void create_reviewExists_throwsRecordNotFoundException() {
        // Arrange
        when(productRepository.existsById("prod1")).thenReturn(true);
        when(userRepository.existsById("user1")).thenReturn(true);
        when(reviewRepository.existsByProductIdAndUserId("prod1", "user1")).thenReturn(true);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> reviewService.create(reviewRequestDto));
        assertEquals("Review already exists", exception.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void update_success_returnsReviewResponseDto() {
        // Arrange
        when(reviewRepository.findById("rev1")).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(mapper.map(review, ReviewResponseDto.class)).thenReturn(reviewResponseDto);

        // Act
        ReviewResponseDto result = reviewService.update("rev1", reviewRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("rev1", result.getId());
        assertEquals(5, result.getRating());
        assertEquals("Great product!", result.getComment());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(mapper, times(1)).map(review, ReviewResponseDto.class);
    }

    @Test
    void update_reviewNotFound_throwsRecordNotFoundException() {
        // Arrange
        when(reviewRepository.findById("rev1")).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> reviewService.update("rev1", reviewRequestDto));
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void delete_success_deletesReview() {
        // Arrange
        when(reviewRepository.existsById("rev1")).thenReturn(true);

        // Act
        reviewService.delete("rev1");

        // Assert
        verify(reviewRepository, times(1)).deleteById("rev1");
    }

    @Test
    void delete_reviewNotFound_throwsRecordNotFoundException() {
        // Arrange
        when(reviewRepository.existsById("rev1")).thenReturn(false);

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> reviewService.delete("rev1"));
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository, never()).deleteById(any());
    }

    @Test
    void getById_returnsNull() {
        // Act
        ReviewResponseDto result = reviewService.getById("rev1");

        // Assert
        assertNull(result);
        verifyNoInteractions(reviewRepository, productRepository, userRepository, mapper);
    }

    @Test
    void getAll_success_returnsPagedReviews() {
        // Arrange
        Page<Review> reviewPage = new PageImpl<>(List.of(review), pageable, 1);
        when(reviewRepository.findBy(any(Specification.class), any())).thenReturn(reviewPage);
        when(mapper.map(review, ReviewResponseDto.class)).thenReturn(reviewResponseDto);

        // Act
        Page<ReviewResponseDto> result = reviewService.getAll(reviewFilter, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("rev1", result.getContent().get(0).getId());
        verify(reviewRepository, times(1)).findBy(any(Specification.class), any());
        verify(mapper, times(1)).map(review, ReviewResponseDto.class);
    }

    @Test
    void getAll_emptyPage_returnsEmpty() {
        // Arrange
        Page<Review> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(reviewRepository.findBy(any(Specification.class), any())).thenReturn(emptyPage);

        // Act
        Page<ReviewResponseDto> result = reviewService.getAll(reviewFilter, pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(reviewRepository, times(1)).findBy(any(Specification.class), any());
        verifyNoInteractions(mapper);
    }

    @Test
    void isEligibleForReview_noReviewExists_returnsTrue() {
        // Arrange
        when(reviewRepository.existsByProductIdAndUserId("prod1", "user1")).thenReturn(false);

        // Act
        boolean result = reviewService.isEligibleForReview("prod1", "user1");

        // Assert
        assertTrue(result);
        verify(reviewRepository, times(1)).existsByProductIdAndUserId("prod1", "user1");
    }

    @Test
    void isEligibleForReview_reviewExists_returnsFalse() {
        // Arrange
        when(reviewRepository.existsByProductIdAndUserId("prod1", "user1")).thenReturn(true);

        // Act
        boolean result = reviewService.isEligibleForReview("prod1", "user1");

        // Assert
        assertFalse(result);
        verify(reviewRepository, times(1)).existsByProductIdAndUserId("prod1", "user1");
    }
}