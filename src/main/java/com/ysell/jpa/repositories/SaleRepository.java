package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.SaleEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SaleRepository extends ActiveJpaRepository<SaleEntity> {

    List<SaleEntity> findByCreatedAtGreaterThanAndOrder_OrganisationIdIn(Instant lastSyncDate, Collection<UUID> organisationIds);

    List<SaleEntity> findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrder_OrganisationIdIn(Instant lastSyncDate, Instant lastCreatedSyncDate, Collection<UUID> organisationIds);
}
