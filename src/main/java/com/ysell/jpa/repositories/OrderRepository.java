package com.ysell.jpa.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysell.jpa.entities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

	List<OrderEntity> findByOrganisationId(long organisationId);

	List<OrderEntity> findByCreatedAtGreaterThanAndOrganisationIdIn(Date lastSyncDate, Collection<Long> organisationIds);

	List<OrderEntity> findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrganisationIdIn(Date lastSyncDate, Date lastCreatedSyncDate, Collection<Long> organisationIds);
}
