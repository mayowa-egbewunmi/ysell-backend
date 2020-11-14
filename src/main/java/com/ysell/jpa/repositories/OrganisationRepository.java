package com.ysell.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.UserEntity;

public interface OrganisationRepository extends JpaRepository<OrganisationEntity, Long> {

	List<OrganisationEntity> findByEmailIgnoreCase(String email);
	
	List<OrganisationEntity> findByUsers(UserEntity userEntity);
}
