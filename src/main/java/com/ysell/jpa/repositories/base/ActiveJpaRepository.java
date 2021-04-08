package com.ysell.jpa.repositories.base;

import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import com.ysell.modules.common.utilities.ServiceUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.UUID;


@NoRepositoryBean
public interface ActiveJpaRepository<TEntity extends ActiveClientAuditableEntity> extends JpaRepository<TEntity, UUID> {

	@Transactional(readOnly = true)
	default TEntity findById(UUID id, String modelName) {
		return findById(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException(modelName, id));
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