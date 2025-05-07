package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.api.response.ProductVariantResponseDto;
import com.nr.ecommercebe.module.catalog.model.ProductVariant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    List<ProductVariantResponseDto> findByProductIdAndDeletedFalse(String productId);
    Optional<ProductVariant> findByIdAndDeletedFalse(String variantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pv FROM product_variants pv WHERE pv.id = :variantId")
    Optional<ProductVariant> findByIdWithLock(String variantId);
}
