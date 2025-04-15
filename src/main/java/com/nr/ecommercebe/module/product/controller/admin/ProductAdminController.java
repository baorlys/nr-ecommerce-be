package com.nr.ecommercebe.module.product.controller.admin;

import com.nr.ecommercebe.module.product.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.product.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.product.service.ProductService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/products")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductAdminController {
    ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDetailResponseDto> createProduct(@RequestBody ProductRequestDto request) {
        return ResponseEntity.ok(productService.create(request));
    }

}
