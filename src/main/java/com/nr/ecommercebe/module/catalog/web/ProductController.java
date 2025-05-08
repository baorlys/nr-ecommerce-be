package com.nr.ecommercebe.module.catalog.web;

import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.shared.dto.PagedResponseSuccess;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "Endpoints for retrieving and managing products")
public class ProductController {
    ProductService productService;

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Fetches a list of products with optional filters and pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched products"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<PagedResponseSuccess<ProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute ProductFilter filter) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponseDto> productPage = productService.getAll(filter, pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Products fetched successfully", productPage));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by ID",
            description = "Fetches a product by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched product"),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ProductDetailResponseDto> getProductById(@PathVariable String id) {
        ProductDetailResponseDto product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("bySlug/{slug}")
    @Operation(
            summary = "Get product by slug",
            description = "Fetches a product by its slug",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched product"),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ProductDetailResponseDto> getProductBySlug(@PathVariable String slug) {
        ProductDetailResponseDto product = productService.getBySlug(slug);
        return ResponseEntity.ok(product);
    }
}
