package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.PaymentEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends ActiveJpaRepository<PaymentEntity> {

	List<PaymentEntity> findByCreatedAtGreaterThanAndOrder_OrganisationIdIn(Instant lastSyncDate, Collection<UUID> organisationIds);

	List<PaymentEntity> findByUpdatedAtGreaterThanAndCreatedAtLessThanAndOrder_OrganisationIdIn(Instant lastSyncDate, Instant lastCreatedSyncDate, Collection<UUID> organisationIds);
}
