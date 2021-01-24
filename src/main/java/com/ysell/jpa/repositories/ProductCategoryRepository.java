package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.ProductCategoryEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import com.ysell.jpa.repositories.base.NameRepository;

public interface ProductCategoryRepository extends ActiveJpaRepository<ProductCategoryEntity>, NameRepository<ProductCategoryEntity> {
}
