package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import com.ysell.jpa.repositories.base.NameRepository;

import java.time.Instant;
import java.util.*;

public interface ProductRepository extends ActiveJpaRepository<ProductEntity>, NameRepository<ProductEntity> {

	List<ProductEntity> findByOrganisationIdIn(Set<UUID> organisationId);

	Optional<ProductEntity> findFirstByNameIgnoreCaseAndOrganisationId(String name, UUID organisationId);

	boolean existsByNameIgnoreCaseAndOrganisationId(String name, UUID organisationId);

	List<ProductEntity> findByCreatedAtGreaterThanAndOrganisationIdIn(Instant lastSyncDate, Collection<UUID> organisationIds);

	List<ProductEntity> findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(Instant lastSyncDate, Instant lastCreatedSyncDate, Collection<UUID> organisationIds);
}
