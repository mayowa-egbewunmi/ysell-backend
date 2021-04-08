package com.ysell.jpa.repositories.base;

import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import com.ysell.modules.common.utilities.ServiceUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@NoRepositoryBean
public interface InactiveJpaRepository<TEntity extends ActiveClientAuditableEntity> extends JpaRepository<TEntity, UUID> {

	@Transactional
	default TEntity unDeleteById(UUID id, String modelName) {
		TEntity entity = findById(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException(modelName, id));
		entity.setActive(true);

		return save(entity);
	}


	@Transactional
	default TEntity unDelete(TEntity entity) {
		return unDeleteById(entity.getId(), entity.getTableName());
	}
}
