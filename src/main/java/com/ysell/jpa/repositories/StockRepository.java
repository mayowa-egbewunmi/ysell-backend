package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.StockEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface StockRepository extends ActiveJpaRepository<StockEntity> {

	Page<StockEntity> findByCreatedAtGreaterThanEqual(Instant earliestCreatedDate, Pageable pageable);
}
