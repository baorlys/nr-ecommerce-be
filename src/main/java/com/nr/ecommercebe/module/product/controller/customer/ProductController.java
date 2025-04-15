package com.nr.ecommercebe.module.product.controller.customer;

import com.nr.ecommercebe.module.product.dto.request.ProductFilter;
import com.nr.ecommercebe.module.product.dto.response.PagedResponseSuccess;
import com.nr.ecommercebe.module.product.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.product.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.product.dto.shared.ReviewDto;
import com.nr.ecommercebe.module.product.service.ProductService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductController {
    ProductService productService;

    @GetMapping
    public ResponseEntity<PagedResponseSuccess<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute ProductFilter filter) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponseDto> productPage = productService.getAll(filter, pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Products fetched successfully",productPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponseDto> getProductById(@PathVariable String id) {

        ProductDetailResponseDto product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<PagedResponseSuccess<ReviewDto>> getProductReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String id) {

        Page<ReviewDto> reviewPage = productService.getProductReviews(id);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Reviews fetched successfully",reviewPage));
    }





}
