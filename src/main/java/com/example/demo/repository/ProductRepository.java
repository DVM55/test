package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Modifying
    @Transactional
    @Query("update Product p set p.stock = :stock where p.productId = :productId")
    int updateStock(@Param("productId") Long productId, @Param("stock") Integer stock);
}
