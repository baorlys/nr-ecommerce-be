package com.nr.ecommercebe.web.admin;

import com.nr.ecommercebe.common.model.PagedResponseSuccess;
import com.nr.ecommercebe.module.catalog.api.CategoryService;
import com.nr.ecommercebe.module.catalog.api.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;
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
@RequestMapping("api/v1/admin/categories")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryAdminController {
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto request) {
        CategoryResponseDto response = categoryService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("api/v1/categories/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@Valid @RequestBody CategoryRequestDto request,
                                                              @PathVariable String id) {
        CategoryResponseDto response = categoryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<PagedResponseSuccess<AdminCategoryFlatResponseDto>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AdminCategoryFlatResponseDto> categories = categoryService.getAllFlatForAdmin(pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Categories fetched successfully", categories));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
