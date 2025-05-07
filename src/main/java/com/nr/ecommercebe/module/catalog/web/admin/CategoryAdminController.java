package com.nr.ecommercebe.module.catalog.web.admin;

import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryFilter;
import com.nr.ecommercebe.shared.dto.PagedResponseSuccess;
import com.nr.ecommercebe.module.catalog.application.service.CategoryService;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/admin/categories")
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Admin - Categories", description = "Admin operations for managing product categories")
public class CategoryAdminController {
    CategoryService categoryService;

    @PostMapping
    @Operation(
            summary = "Create a new category",
            description = "Adds a new category to the catalog and returns its details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload")
            }
    )
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryRequestDto request) {
        CategoryResponseDto response = categoryService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/admin/categories/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Update a category",
            description = "Updates an existing category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @Valid @RequestBody CategoryRequestDto request,
            @Parameter(description = "Category ID", example = "abc123")
            @PathVariable String id) {
        CategoryResponseDto response = categoryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Get paginated list of categories",
            description = "Returns a paginated flat list of categories with optional filters"
    )
    public ResponseEntity<PagedResponseSuccess<AdminCategoryFlatResponseDto>> getAllCategories(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Optional filter parameters")
            @ModelAttribute CategoryFilter filter) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AdminCategoryFlatResponseDto> categories = categoryService.getAllFlatForAdmin(filter, pageRequest);
        return ResponseEntity.ok(new PagedResponseSuccess<>("Categories fetched successfully", categories));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete a category",
            description = "Deletes an existing category by its ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID", example = "abc123")
            @PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
