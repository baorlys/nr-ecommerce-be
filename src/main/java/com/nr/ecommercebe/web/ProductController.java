package com.nr.ecommercebe.web;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.common.model.PagedResponseSuccess;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductResponseDto;
import com.nr.ecommercebe.module.catalog.api.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
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





}
