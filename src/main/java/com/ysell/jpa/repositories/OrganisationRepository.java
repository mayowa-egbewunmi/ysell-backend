package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import com.ysell.jpa.repositories.base.EmailRepository;
import com.ysell.jpa.repositories.base.NameRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrganisationRepository
		extends ActiveJpaRepository<OrganisationEntity>, NameRepository<OrganisationEntity>, EmailRepository<OrganisationEntity> {

	List<OrganisationEntity> findByUsersIdIn(Set<UUID> userIds);
}
