package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import com.ysell.jpa.repositories.base.NameRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends ActiveJpaRepository<ProductEntity>, NameRepository<ProductEntity> {

	List<ProductEntity> findByOrganisationIdIn(Set<UUID> organisationId);

	boolean existsByIdAndOrganisationIdIn(UUID id, Set<UUID> organisationId);

	List<ProductEntity> findByNameIgnoreCaseAndOrganisationId(String name, UUID organisationId);

	boolean existsByNameIgnoreCaseAndOrganisationId(String name, UUID organisationId);

	List<ProductEntity> findByCreatedAtGreaterThanAndOrganisationIdIn(LocalDate lastSyncDate, Collection<UUID> organisationIds);

	List<ProductEntity> findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(LocalDate lastSyncDate, LocalDate lastCreatedSyncDate, Collection<UUID> organisationIds);
}
