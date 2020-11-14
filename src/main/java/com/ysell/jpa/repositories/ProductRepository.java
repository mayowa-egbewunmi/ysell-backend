package com.ysell.jpa.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysell.jpa.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	List<ProductEntity> findByOrganisationId(long organisationId);

	Optional<ProductEntity> findByNameIgnoreCaseAndOrganisationId(String name, long organisationId);

	Optional<ProductEntity> findByIdEqualsAndOrganisationIdIn(long id, Collection<Long> organisationIds);

	List<ProductEntity> findByCreatedAtGreaterThanAndOrganisationIdIn(Date lastSyncDate, Collection<Long> organisationIds);

	List<ProductEntity> findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(Date lastSyncDate, Date lastCreatedSyncDate, Collection<Long> organisationIds);
}
