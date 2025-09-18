package com.example.CartService.Repository;

import com.example.CartService.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserName(String userName);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM cart_items WHERE cart_id = :cartId AND product_id IN (:productIds)",
            nativeQuery = true
    )
    void deleteByCartIdAndProductIds(@Param("cartId") String cartId,
                                     @Param("productIds") List<String> productIds);
}

