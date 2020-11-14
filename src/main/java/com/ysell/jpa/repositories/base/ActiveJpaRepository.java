package com.ysell.jpa.repositories.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.ysell.jpa.entities.base.ActiveEntity;

@NoRepositoryBean
public interface ActiveJpaRepository<TEntity extends ActiveEntity, TId> extends JpaRepository<TEntity, TId> {

	@Query("select e from #{#entityName} e where e.active=true")
	@Transactional(readOnly = true)
	public List<TEntity> findAllActive();
	
	@Query("select e from #{#entityName} e where e.active=false")
	@Transactional(readOnly = true)
	public List<TEntity> findAllInactive(); 
}
