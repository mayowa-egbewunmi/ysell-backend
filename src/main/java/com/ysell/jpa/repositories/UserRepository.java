package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import com.ysell.jpa.repositories.base.EmailRepository;
import com.ysell.jpa.repositories.base.NameRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends ActiveJpaRepository<UserEntity>, NameRepository<UserEntity>, EmailRepository<UserEntity> {
	
	List<UserEntity> findByOrganisationsId(UUID organisationId);
}
