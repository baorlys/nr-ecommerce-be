package com.nr.ecommercebe.web.admin;

import com.nr.ecommercebe.shared.model.PagedResponseSuccess;
import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductImageRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductVariantRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/admin/products")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductAdminController {
    ProductService productService;

    /**
     * Get paginated list of products for admin with optional filtering.
     *
     * @param page    current page number (0-based)
     * @param size    page size
     * @param filter  filtering criteria (e.g., by category, name, etc.)
     * @return        paged list of products wrapped in a success response
     */
    @GetMapping
    public ResponseEntity<PagedResponseSuccess<AdminProductResponseDto>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute ProductFilter filter) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AdminProductResponseDto> productPage = productService.getAllForAdmin(filter, pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Products fetched successfully", productPage));
    }

    /**
     * Create a new product with basic information.
     *
     * @param request product creation request payload
     * @return        created product details with 201 Created response
     */
    @PostMapping
    public ResponseEntity<ProductDetailResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        ProductDetailResponseDto response = productService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Update a product's basic information (excluding images or variants).
     *
     * @param id      product ID
     * @param request update request payload
     * @return        updated product details
     */
    @PatchMapping("{id}")
    public ResponseEntity<ProductDetailResponseDto> updateProduct(@PathVariable String id,
                                                                  @Valid @RequestBody ProductRequestDto request) {
        ProductDetailResponseDto response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a new variant to a product.
     *
     * @param id      product ID
     * @param request new variant data
     * @return        201 Created response
     */
    @PostMapping("{id}/variants")
    public ResponseEntity<Void> addProductVariant(@PathVariable String id,
                                                  @Valid @RequestBody ProductVariantRequestDto request) {
        productService.addVariant(id, request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri()).build();
    }

    /**
     * Update a specific variant of a product.
     *
     * @param variantId variant ID
     * @param request   update payload
     * @return          200 OK response
     */
    @PutMapping("variants/{variantId}")
    public ResponseEntity<Void> updateProductVariant(@PathVariable String variantId,
                                                     @Valid @RequestBody ProductVariantRequestDto request) {
        productService.updateVariant(variantId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a specific variant from a product.
     *
     * @param variantId variant ID to delete
     * @return          204 No Content response
     */
    @DeleteMapping("variants/{variantId}")
    public ResponseEntity<Void> deleteProductVariant(@PathVariable String variantId) {
        productService.deleteVariant(variantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add an image to a product.
     *
     * @param id      product ID
     * @param request image upload data
     * @return        201 Created response
     */
    @PostMapping("{id}/images")
    public ResponseEntity<Void> addProductImage(@PathVariable String id,
                                                @Valid @RequestBody ProductImageRequestDto request) {
        productService.addImage(id, request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri()).build();
    }

    /**
     * Delete a specific image from a product.
     *
     * @param imageId image ID to delete
     * @return        204 No Content response
     */
    @DeleteMapping("images/{imageId}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable String imageId) {
        productService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }




}
