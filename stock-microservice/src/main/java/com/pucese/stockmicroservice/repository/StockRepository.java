package com.pucese.stockmicroservice.repository;

import com.pucese.stockmicroservice.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity, Long> {

    Optional<StockEntity> findByCode(String code);
}
