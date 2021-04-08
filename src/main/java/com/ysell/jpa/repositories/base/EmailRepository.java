package com.ysell.jpa.repositories.base;

import com.ysell.jpa.entities.base.ClientAuditableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface EmailRepository<TEntity extends ClientAuditableEntity> extends JpaRepository<TEntity, UUID> {

	boolean existsByEmailIgnoreCase(String email);

	Optional<TEntity> findFirstByEmailIgnoreCase(String email);
}
