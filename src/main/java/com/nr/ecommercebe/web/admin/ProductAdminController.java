package com.nr.ecommercebe.web.admin;

import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    @PostMapping
    public ResponseEntity<ProductDetailResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        ProductDetailResponseDto response = productService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("api/v1/products/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDetailResponseDto> updateProduct(@Valid @RequestBody ProductRequestDto request,
                                                                  @PathVariable String id) {
        ProductDetailResponseDto response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

}
