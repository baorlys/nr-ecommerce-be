package com.nr.ecommercebe.module.catalog.api.mapper;

import com.nr.ecommercebe.module.catalog.api.request.ProductImageRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductVariantRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductImageResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductVariantResponseDto;
import com.nr.ecommercebe.module.catalog.model.Category;
import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.catalog.model.ProductImage;
import com.nr.ecommercebe.module.catalog.model.ProductVariant;
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
                            source.getWeight(),
                            source.getUnit(),
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
        return product;
    }

    public ProductVariant toVariantEntity(ProductVariantRequestDto dto) {
        return mapper.map(dto, ProductVariant.class);
    }

    public ProductImage toImageEntity(ProductImageRequestDto dto) {
        return mapper.map(dto, ProductImage.class);
    }


    public ProductDetailResponseDto toDto(Product product) {
        ProductDetailResponseDto dto = mapper.map(product, ProductDetailResponseDto.class);
        dto.setImages(product.getProductImages().stream()
                .map(image -> mapper.map(image, ProductImageResponseDto.class))
                .toList());
        dto.setVariants(product.getProductVariants().stream()
                .map(variant -> mapper.map(variant, ProductVariantResponseDto.class))
                .toList());
        return dto;
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