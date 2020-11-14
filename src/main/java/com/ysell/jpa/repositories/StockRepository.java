package com.ysell.jpa.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ysell.jpa.entities.StockEntity;

public interface StockRepository extends JpaRepository<StockEntity, Long>  {

	List<StockEntity> findByCreatedAtGreaterThanEqual(Date earliestCreatedDate, Sort sort);
}
