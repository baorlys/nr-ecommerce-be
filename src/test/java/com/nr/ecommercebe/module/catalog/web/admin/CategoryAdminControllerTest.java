package com.nr.ecommercebe.module.catalog.web.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.application.service.CategoryService;
import com.nr.ecommercebe.module.user.application.service.authentication.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryAdminController.class)
@WithMockUser(roles = "ADMIN")
 class CategoryAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryRequestDto validCategoryRequest;
    private CategoryResponseDto categoryResponse;
    private AdminCategoryFlatResponseDto categoryFlatResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        validCategoryRequest = new CategoryRequestDto();
        validCategoryRequest.setName("Electronics");
        validCategoryRequest.setDescription("Electronic products");
        validCategoryRequest.setParentId(null);

        categoryResponse = new CategoryResponseDto();
        categoryResponse.setId("cat123");
        categoryResponse.setName("Electronics");
        categoryResponse.setSlug("electronics");
        categoryResponse.setDescription("Electronic products");
        categoryResponse.setParentId(null);

        categoryFlatResponse = new AdminCategoryFlatResponseDto();
        categoryFlatResponse.setId("cat123");
        categoryFlatResponse.setName("Electronics");
        categoryFlatResponse.setSlug("electronics");
        categoryFlatResponse.setDescription("Electronic products");
    }

    @Test
    void createCategory_Success() throws Exception {
        when(categoryService.create(any(CategoryRequestDto.class))).thenReturn(categoryResponse);

        mockMvc.perform(post("/api/v1/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("cat123")))
                .andExpect(jsonPath("$.name", is("Electronics")))
                .andExpect(jsonPath("$.slug", is("electronics")))
                .andExpect(jsonPath("$.description", is("Electronic products")))
                .andExpect(header().exists("Location"));

        verify(categoryService, times(1)).create(any(CategoryRequestDto.class));
    }

    @Test
    void updateCategory_Success() throws Exception {
        when(categoryService.update(eq("cat123"), any(CategoryRequestDto.class))).thenReturn(categoryResponse);

        mockMvc.perform(put("/api/v1/admin/categories/cat123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("cat123")))
                .andExpect(jsonPath("$.name", is("Electronics")))
                .andExpect(jsonPath("$.slug", is("electronics")))
                .andExpect(jsonPath("$.description", is("Electronic products")));

        verify(categoryService, times(1)).update(eq("cat123"), any(CategoryRequestDto.class));
    }

    @Test
    void getAllCategories_Success() throws Exception {
        List<AdminCategoryFlatResponseDto> categories = Arrays.asList(categoryFlatResponse);
        Page<AdminCategoryFlatResponseDto> pagedResponse = new PageImpl<>(categories);

        when(categoryService.getAllFlatForAdmin(any(CategoryFilter.class), any(PageRequest.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/admin/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Categories fetched successfully")))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].id", is("cat123")))
                .andExpect(jsonPath("$.data.content[0].name", is("Electronics")))
                .andExpect(jsonPath("$.data.totalElements", is(1)));

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(categoryService, times(1)).getAllFlatForAdmin(any(CategoryFilter.class), pageRequestCaptor.capture());
        PageRequest capturedPageRequest = pageRequestCaptor.getValue();
        assertEquals(0, capturedPageRequest.getPageNumber());
        assertEquals(10, capturedPageRequest.getPageSize());
    }

    @Test
    void deleteCategory_Success() throws Exception {
        doNothing().when(categoryService).delete("cat123");

        mockMvc.perform(delete("/api/v1/admin/categories/cat123"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).delete("cat123");
    }

    @Test
    void createCategory_BadRequest() throws Exception {
        // Create an invalid request with null name
        CategoryRequestDto invalidRequest = new CategoryRequestDto();
        // Don't set required fields

        mockMvc.perform(post("/api/v1/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).create(any(CategoryRequestDto.class));
    }

    @Test
    void getAllCategories_WithFilter() throws Exception {
        List<AdminCategoryFlatResponseDto> categories = Arrays.asList(categoryFlatResponse);
        Page<AdminCategoryFlatResponseDto> pagedResponse = new PageImpl<>(categories);

        ArgumentCaptor<CategoryFilter> filterCaptor = ArgumentCaptor.forClass(CategoryFilter.class);

        when(categoryService.getAllFlatForAdmin(any(CategoryFilter.class), any(PageRequest.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/admin/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getAllFlatForAdmin(filterCaptor.capture(), any(PageRequest.class));
        CategoryFilter capturedFilter = filterCaptor.getValue();
         assertEquals("Electronics", capturedFilter.getSearch());
    }
}