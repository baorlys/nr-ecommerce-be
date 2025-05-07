package com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.product;

import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.application.domain.ProductImage;
import com.nr.ecommercebe.module.catalog.application.domain.ProductVariant;
import com.nr.ecommercebe.module.catalog.application.domain.enums.field.ProductField;
import com.nr.ecommercebe.module.catalog.application.domain.enums.field.CategoryField;
import com.nr.ecommercebe.module.catalog.application.domain.enums.field.ProductImageField;
import com.nr.ecommercebe.module.catalog.application.domain.enums.field.ProductVariantField;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductSortOption;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryBasicInfoResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.review.application.domain.Review;
import com.nr.ecommercebe.module.review.application.domain.enums.ReviewField;
import com.nr.ecommercebe.shared.domain.enums.BaseEntityField;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductResponseDto> findAllAndFilterWithDto(ProductFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ProductResponseDto> query = cb.createQuery(ProductResponseDto.class);
        Root<Product> root = query.from(Product.class);
        Join<Product, ProductImage> imageJoin = root.join(ProductField.IMAGES.toString(), JoinType.LEFT);
        Join<Product, ProductVariant> variantJoin = root.join(ProductField.VARIANTS.toString(), JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, root, filter);
        query.select(cb.construct(
                        ProductResponseDto.class,
                        root.get(BaseEntityField.ID.toString()),
                        root.get(ProductField.NAME.toString()),
                        root.get(ProductField.SLUG.toString()),
                        imageJoin.get(ProductImageField.IMAGE_URL.toString()),
                        cb.min(variantJoin.get(ProductVariantField.PRICE.toString()))
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])),
                        cb.isTrue(imageJoin.get(ProductImageField.IS_PRIMARY.toString())),
                        cb.isFalse(variantJoin.get(BaseEntityField.DELETED.toString())))
                .groupBy(
                        root.get(BaseEntityField.ID.toString()),
                        root.get(ProductField.NAME.toString()),
                        root.get(ProductField.SLUG.toString()),
                        imageJoin.get(ProductImageField.IMAGE_URL.toString()));

        applySorting(query, cb, root, filter);
        List<ProductResponseDto> content = getPagedResult(query, pageable);
        long total = countProducts(filter);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<ProductDetailResponseDto> findByIdWithDto(String id) {
        return findProductDetailBy(BaseEntityField.ID.toString(), id);
    }

    @Override
    public Optional<ProductDetailResponseDto> findBySlugWithDto(String slug) {
        return findProductDetailBy(ProductField.SLUG.toString(), slug);
    }

    @Override
    public Page<AdminProductResponseDto> findAllAndFilterForAdminWithDto(ProductFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<AdminProductResponseDto> query = cb.createQuery(AdminProductResponseDto.class);
        Root<Product> root = query.from(Product.class);
        Join<Product, Category> categoryJoin = root.join(ProductField.CATEGORY.toString(), JoinType.LEFT);
        Join<Product, ProductImage> imageJoin = root.join(ProductField.IMAGES.toString(), JoinType.LEFT);
        Join<Product, ProductVariant> variantJoin = root.join(ProductField.VARIANTS.toString(), JoinType.LEFT);
        Join<Product, Review> reviewJoin = root.join(ProductField.REVIEWS.toString(), JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, root, filter);
        query.select(cb.construct(
                        AdminProductResponseDto.class,
                        root.get(BaseEntityField.ID.toString()),
                        root.get(ProductField.NAME.toString()),
                        root.get(ProductField.SLUG.toString()),
                        imageJoin.get(ProductImageField.IMAGE_URL.toString()),
                        categoryJoin.get(BaseEntityField.ID.toString()),
                        categoryJoin.get(CategoryField.NAME.toString()),
                        root.get(ProductField.IS_FEATURED.toString()),
                        cb.countDistinct( variantJoin.get(BaseEntityField.ID.toString())),
                        cb.avg(reviewJoin.get(ReviewField.RATING.toString())),
                        cb.countDistinct(reviewJoin.get(BaseEntityField.ID.toString())),
                        root.get(BaseEntityField.CREATED_ON.toString()),
                        root.get(BaseEntityField.UPDATED_ON.toString())
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])),
                        cb.isTrue(imageJoin.get(ProductImageField.IS_PRIMARY.toString())),
                        cb.isFalse(variantJoin.get(BaseEntityField.DELETED.toString())))
                .groupBy(
                        root.get(BaseEntityField.ID.toString()),
                        root.get(ProductField.NAME.toString()),
                        root.get(ProductField.SLUG.toString()),
                        imageJoin.get(ProductImageField.IMAGE_URL.toString()),
                        categoryJoin.get(BaseEntityField.ID.toString()),
                        categoryJoin.get(CategoryField.NAME.toString()),
                        root.get(ProductField.IS_FEATURED.toString()),
                        root.get(BaseEntityField.CREATED_ON.toString()),
                        root.get(BaseEntityField.UPDATED_ON.toString())
                );
        List<AdminProductResponseDto> content = getPagedResult(query, pageable);
        long total = countProducts(filter);

        return new PageImpl<>(content, pageable, total);
    }



    // -------------------------- Private Utility Methods --------------------------
    private Optional<ProductDetailResponseDto> findProductDetailBy(String fieldName, Object value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductDetailResponseDto> query = cb.createQuery(ProductDetailResponseDto.class);

        Root<Product> root = query.from(Product.class);
        Join<Product, Review> reviewJoin = root.join(ProductField.REVIEWS.toString(), JoinType.LEFT);
        Join<Product, Category> categoryJoin = root.join(ProductField.CATEGORY.toString(), JoinType.LEFT);

        query.select(cb.construct(
                        ProductDetailResponseDto.class,
                        root.get(BaseEntityField.ID.toString()),
                        root.get(ProductField.NAME.toString()),
                        root.get(ProductField.SHORT_DESCRIPTION.toString()),
                        root.get(ProductField.DESCRIPTION.toString()),
                        cb.construct(
                                CategoryBasicInfoResponseDto.class,
                                categoryJoin.get(BaseEntityField.ID.toString()),
                                categoryJoin.get(CategoryField.NAME.toString()),
                                categoryJoin.get(CategoryField.SLUG.toString()),
                                categoryJoin.get(CategoryField.IMAGE_URL.toString()),
                                categoryJoin.get(CategoryField.PARENT.toString()).get(BaseEntityField.ID.toString())
                        ),
                        cb.avg(reviewJoin.get(ReviewField.RATING.toString())),
                        cb.countDistinct(reviewJoin.get(BaseEntityField.ID.toString())),
                        root.get(ProductField.IS_FEATURED.toString())
                ))
                .where(cb.equal(root.get(fieldName), value))
                .groupBy(
                        root.get(BaseEntityField.ID.toString()),
                        root.get(ProductField.NAME.toString()),
                        root.get(ProductField.DESCRIPTION.toString()),
                        root.get(ProductField.SHORT_DESCRIPTION.toString()),
                        categoryJoin.get(BaseEntityField.ID.toString()),
                        categoryJoin.get(CategoryField.NAME.toString()),
                        categoryJoin.get(CategoryField.SLUG.toString()),
                        categoryJoin.get(CategoryField.IMAGE_URL.toString()),
                        categoryJoin.get(CategoryField.PARENT.toString()).get(BaseEntityField.ID.toString()),
                        root.get(ProductField.IS_FEATURED.toString())
                );

        List<ProductDetailResponseDto> results = entityManager.createQuery(query).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }


    private List<Predicate> buildPredicates(CriteriaBuilder cb,
                                            Root<Product> root,
                                             ProductFilter filter) {
        return new ArrayList<>(new ProductPredicateBuilder(filter).build(cb, root));
    }

    private void applySorting(CriteriaQuery<?> query, CriteriaBuilder cb, Root<Product> root, ProductFilter filter) {
        ProductSortOption sortOption = Optional.ofNullable(filter.getSortBy()).orElse(ProductSortOption.NEWEST);
        switch (sortOption) {
            case PRICE_ASC -> query.orderBy(cb.asc(cb.min(root.get(ProductField.VARIANTS.toString()).get(ProductVariantField.PRICE.toString()))));
            case PRICE_DESC -> query.orderBy(cb.desc(cb.min(root.get(ProductField.VARIANTS.toString()).get(ProductVariantField.PRICE.toString()))));
            case NEWEST -> query.orderBy(cb.desc(root.get(BaseEntityField.CREATED_ON.toString())));
        }
    }

    private <T> List<T> getPagedResult(CriteriaQuery<T> query, Pageable pageable) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    private long countProducts(ProductFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> root = countQuery.from(Product.class);
        Join<Product, ProductImage> imageJoin = root.join(ProductField.IMAGES.toString());

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.isTrue(imageJoin.get(ProductImageField.IS_PRIMARY.toString())));
        countPredicates.addAll(new ProductPredicateBuilder(filter).build(cb, root));

        countQuery.select(cb.countDistinct(root.get(BaseEntityField.ID.toString())))
                .where(cb.and(countPredicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
