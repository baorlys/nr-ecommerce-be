package com.nr.ecommercebe.module.catalog.web.admin;

import com.nr.ecommercebe.shared.dto.PagedResponseSuccess;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.service.ProductService;
import com.nr.ecommercebe.shared.util.URIUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/v1/admin/products")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Admin - Products", description = "Admin operations for managing products")
public class ProductAdminController {
    ProductService productService;

    @GetMapping
    @Operation(
            summary = "Get paginated list of products",
            description = "Returns a paginated list of products for admin with optional filtering"
    )
    public ResponseEntity<PagedResponseSuccess<AdminProductResponseDto>> getProducts(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filter by product attributes like category or name,... etc.")
            @ModelAttribute ProductFilter filter) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AdminProductResponseDto> productPage = productService.getAllForAdmin(filter, pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Products fetched successfully", productPage));
    }

    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Adds a new product to the catalog and returns its details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload")
            }
    )
    public ResponseEntity<ProductDetailResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto request) {
        ProductDetailResponseDto response = productService.create(request);
        URI location = URIUtil.buildLocationUri("/{id}", response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Update product",
            description = "Updates an existing product by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    public ResponseEntity<ProductDetailResponseDto> updateProduct(
            @Parameter(description = "Product ID", example = "12345")
            @PathVariable String id,

            @Valid @RequestBody ProductRequestDto request) {
        ProductDetailResponseDto response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete product",
            description = "Deletes an existing product by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", example = "12345")
            @PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}





