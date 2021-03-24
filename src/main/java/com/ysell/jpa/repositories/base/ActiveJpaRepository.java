package com.ysell.jpa.repositories.base;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.modules.common.utilities.ServiceUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface ActiveJpaRepository<TEntity extends ActiveAuditableEntity>
		extends JpaRepository<TEntity, UUID> {

	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active = 1")
	List<TEntity> findAll();


	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active = 1")
	Page<TEntity> findAll(Pageable pageable);


	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active = 1")
	List<TEntity> findAll(Sort sort);


	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active = 1")
	<S extends TEntity> List<S> findAll(Example<S> example);


	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.id in ?1 and e.active = 1")
	List<TEntity> findAllById(Iterable<UUID> ids);


	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active <> 1")
	List<TEntity> findInActive();


	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active = ?1")
	List<TEntity> findAllByActiveStatus(boolean isActive);


	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e")
	List<TEntity> findAllRegardless();


	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active = 1")
	default TEntity findById(UUID id, String modelName) {
		return findById(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException(modelName, id));
	}


	@Transactional
	default TEntity unDeleteById(UUID id) {
		TEntity entity = findById(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Record", id));
		entity.setActive(true);

		return save(entity);
	}

	@Transactional
	default TEntity unDelete(TEntity entity) {
		return unDeleteById(entity.getId());
	}


	@Override
	@Transactional
	default void delete(@Nonnull TEntity entity) {
		entity.setActive(false);
		save(entity);
	}


	@Override
	@Transactional
	default void deleteById(@Nonnull UUID id) {
		TEntity entity = findById(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Record", id));
		delete(entity);
	}


	@Override
	@Transactional
	default void deleteAll(@Nonnull Iterable<? extends TEntity> entities) {
		for (TEntity entity : entities)
			delete(entity);
	}


	@Override
	@Transactional
	default void deleteAll() {
		deleteAll(findAll());
	}


	@Transactional
	@Modifying
	@Query("delete from #{#entityName} e where e.id = ?1")
	void hardDelete(UUID id);
}