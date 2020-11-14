package com.ysell.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysell.jpa.entities.ProductCategoryEntity;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

}
