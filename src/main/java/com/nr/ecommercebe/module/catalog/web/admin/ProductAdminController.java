package com.nr.ecommercebe.module.catalog.web.admin;

import com.nr.ecommercebe.shared.dto.PagedResponseSuccess;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.service.ProductService;
import com.nr.ecommercebe.shared.util.URIUtil;
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
     * Create a new product.
     *
     * @param request product creation request payload
     * @return        created product details with 201 Created response
     */
    @PostMapping
    public ResponseEntity<ProductDetailResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        ProductDetailResponseDto response = productService.create(request);

        URI location = URIUtil.buildLocationUri("/{id}", response.getId());
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Update a product by its ID.
     *
     * @param id      product ID
     * @param request update request payload
     * @return        updated product details
     */
    @PostMapping("{id}")
    public ResponseEntity<ProductDetailResponseDto> updateProduct(@PathVariable String id,
                                                                  @Valid @RequestBody ProductRequestDto request) {
        ProductDetailResponseDto response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product by its ID.
     *
     * @param id product ID to delete
     * @return   204 No Content response
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
