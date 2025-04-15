package com.nr.ecommercebe.module.product.repository.custom;

import com.nr.ecommercebe.module.product.dto.request.ProductFilter;
import com.nr.ecommercebe.module.product.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.product.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.product.entity.Product;
import com.nr.ecommercebe.module.product.entity.ProductImage;
import com.nr.ecommercebe.module.product.entity.ProductVariant;
import com.nr.ecommercebe.module.product.entity.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<ProductResponseDto> findAllAndFilterWithProjection(ProductFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Create the main query
        CriteriaQuery<ProductResponseDto> query = cb.createQuery(ProductResponseDto.class);
        Root<Product> root = query.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.LEFT);

        query
                .select(cb.construct(
                        ProductResponseDto.class,
                        root.get("id"),
                        root.get("name"),
                        imageJoin.get("imageUrl"),
                        cb.min(variantJoin.get("price"))
                ))
                .where(
                        cb.and(
                                cb.isTrue(imageJoin.get("isPrimary")),
                                cb.and(new ProductPredicateBuilder(filter).build(cb,root).toArray(new Predicate[0]))
                        )
                )
                .groupBy(
                        root.get("id"),
                        root.get("name"),
                        imageJoin.get("imageUrl")
                );

        TypedQuery<ProductResponseDto> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<ProductResponseDto> resultList = typedQuery.getResultList();

        // Create the count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        Join<Product, ProductImage> countImageJoin = countRoot.join("productImages");

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.isTrue(countImageJoin.get("isPrimary")));
        countPredicates.addAll(new ProductPredicateBuilder(filter).build(cb, countRoot));

        countQuery.select(cb.countDistinct(countRoot.get("id")))
                .where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, totalCount);

    }

    @Override
    public Optional<ProductDetailResponseDto> findByIdWithProjection(String id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Create the main query
        CriteriaQuery<ProductDetailResponseDto> query = cb.createQuery(ProductDetailResponseDto.class);
        Root<Product> root = query.from(Product.class);

        Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);
        Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.LEFT);
        Join<Product, Review> reviewJoin = root.join("reviews", JoinType.LEFT);

        query
                .select(cb.construct(
                        ProductDetailResponseDto.class,
                        root.get("id"),
                        root.get("name"),
                        root.get("description"),
                        variantJoin.get("price"),
                        root.get("category"),
                        cb.array(variantJoin),
                        cb.array(imageJoin),
                        cb.array(reviewJoin),
                        cb.avg(reviewJoin.get("rating")),
                        cb.count(reviewJoin.get("id")),
                        root.get("isFeatured")
                ))
                .where(
                        cb.and(
                                cb.isTrue(imageJoin.get("isPrimary")),
                                cb.equal(root.get("id"), id)
                        )
                )
                .groupBy(
                        root.get("id"),
                        root.get("name"),
                        root.get("description"),
                        variantJoin.get("price"),
                        root.get("category"),
                        imageJoin,
                        reviewJoin
                );
        TypedQuery<ProductDetailResponseDto> typedQuery = entityManager.createQuery(query);
        List<ProductDetailResponseDto> resultList = typedQuery.getResultList();

        return Optional.ofNullable(resultList.getFirst());
    }


}
