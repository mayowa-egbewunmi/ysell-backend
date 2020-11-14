package com.ysell.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysell.jpa.entities.SaleEntity;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
	
}
