package com.nr.ecommercebe.module.catalog.web;

import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Categories", description = "Endpoints to retrieve product categories")
public class CategoryController {

    CategoryService categoryService;

    @GetMapping()
    @Operation(
            summary = "Get all categories",
            description = "Fetches a list of all categories in the catalog",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched all categories"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by ID",
            description = "Fetches a single category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched category"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }
}
