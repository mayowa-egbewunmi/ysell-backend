package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.OrderEntity;
import com.ysell.jpa.entities.StockEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface StockRepository extends ActiveJpaRepository<StockEntity> {

	Page<StockEntity> findByCreatedAtGreaterThanEqual(LocalDate earliestCreatedDate, Pageable pageable);

	List<OrderEntity> findByCreatedAtGreaterThanAndProductOrganisationIdIn(LocalDate lastSyncDate, Collection<UUID> organisationIds);
}
