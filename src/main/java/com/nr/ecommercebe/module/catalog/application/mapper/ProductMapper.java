package com.nr.ecommercebe.module.catalog.application.mapper;

import com.nr.ecommercebe.module.catalog.application.dto.request.ProductImageRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductVariantRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductImageResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductVariantResponseDto;
import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.application.domain.ProductImage;
import com.nr.ecommercebe.module.catalog.application.domain.ProductVariant;
import com.nr.ecommercebe.shared.util.SlugUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper mapper;

    @PostConstruct
    public void init() {
        mapper.typeMap(ProductImage.class, ProductImageResponseDto.class).setProvider(
                mappingContext -> {
                    ProductImage source = (ProductImage) mappingContext.getSource();
                    return new ProductImageResponseDto(
                            source.getId(),
                            source.getImageUrl(),
                            source.getAltText(),
                            source.getIsPrimary(),
                            source.getSortOrder(),
                            source.getStorageType()
                    );
                }
        );

        mapper.typeMap(ProductVariant.class, ProductVariantResponseDto.class).setProvider(
                mappingContext -> {
                    ProductVariant source = (ProductVariant) mappingContext.getSource();
                    return new ProductVariantResponseDto(
                            source.getId(),
                            source.getName(),
                            source.getPrice(),
                            source.getStockQuantity()
                    );
                }
        );
    }

    public Product toEntity(ProductRequestDto dto) {
        Product product = mapper.map(dto, Product.class);
        Category category = new Category();
        category.setId(dto.getCategoryId());
        product.setCategory(category);
        product.setSlug(SlugUtil.generateSlug(dto.getName()));
        return product;
    }

    public ProductDetailResponseDto toDto(Product product) {
        ProductDetailResponseDto dto = mapper.map(product, ProductDetailResponseDto.class);
        dto.setImages(product.getImages().stream()
                .map(image -> mapper.map(image, ProductImageResponseDto.class))
                .toList());
        dto.setVariants(product.getVariants().stream()
                .map(variant -> mapper.map(variant, ProductVariantResponseDto.class))
                .toList());
        return dto;
    }


    public ProductVariant toVariantEntity(ProductVariantRequestDto dto) {
        return mapper.map(dto, ProductVariant.class);
    }

    public ProductVariantResponseDto toVariantResponseDto(ProductVariant variant) {
        return mapper.map(variant, ProductVariantResponseDto.class);
    }

    public ProductImage toImageEntity(ProductImageRequestDto dto) {
        return mapper.map(dto, ProductImage.class);
    }

    public ProductImageResponseDto toImageResponseDto(ProductImage image) {
        return mapper.map(image, ProductImageResponseDto.class);
    }



    public Set<ProductImage> mapImages(Set<ProductImageRequestDto> imagesDto, Product product) {
        return imagesDto.stream()
                .map(dto -> {
                    ProductImage image = mapper.map(dto, ProductImage.class);
                    image.setProduct(product);
                    return image;
                }).collect(Collectors.toSet());
    }


    public Set<ProductVariant> mapVariants(Set<ProductVariantRequestDto> variantsDto, Product product) {
        return variantsDto.stream()
                .map(dto -> {
                    ProductVariant variant = mapper.map(dto, ProductVariant.class);
                    variant.setProduct(product);
                    return variant;
                }).collect(Collectors.toSet());
    }
}