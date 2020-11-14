package com.ysell.jpa.repositories;

import java.util.List;
import java.util.Optional;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.UserEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;

public interface UserRepository extends ActiveJpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmailIgnoreCase(String email);
	
	List<UserEntity> findByOrganisations(OrganisationEntity organisationEntity);
}
