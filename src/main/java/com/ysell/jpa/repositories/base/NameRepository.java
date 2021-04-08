package com.ysell.jpa.repositories.base;

import com.ysell.jpa.entities.base.ClientAuditableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface NameRepository<TEntity extends ClientAuditableEntity> extends JpaRepository<TEntity, UUID> {

	Optional<TEntity> findFirstByNameIgnoreCase(String name);
}
