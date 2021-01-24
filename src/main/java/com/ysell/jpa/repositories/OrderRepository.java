package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends ActiveJpaRepository<OrderEntity> {

	List<OrderEntity> findByOrganisationIdIn(Set<UUID> organisationId);

	List<OrderEntity> findByCreatedAtGreaterThanAndOrganisationIdIn(LocalDate lastSyncDate, Collection<UUID> organisationIds);

	List<OrderEntity> findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(LocalDate lastSyncDate, LocalDate lastCreatedSyncDate, Collection<UUID> organisationIds);
}
