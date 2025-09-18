package com.example.ProductService.Repository;

import com.example.ProductService.Entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("""
        SELECT p FROM Promotion p
        WHERE p.isActive = true
          AND p.startDate <= CURRENT_TIMESTAMP
          AND p.endDate >= CURRENT_TIMESTAMP
          AND (p.targetType = 'GLOBAL' OR (p.targetType = 'CATEGORY' AND p.targetValue = :category))
    """)
    List<Promotion> findApplicablePromotions(@Param("category") String category);


    @Query("SELECT p FROM Promotion p WHERE p.isActive = true")
    List<Promotion> findActivePromotions();

    @Query("SELECT p FROM Promotion p WHERE p.isActive = false")
    List<Promotion> findInactivePromotions();
}

