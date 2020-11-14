package com.ysell.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ysell.jpa.entities.ResetCodeEntity;

public interface ResetCodeRepository extends JpaRepository<ResetCodeEntity, Long> {
	
	ResetCodeEntity findByUserIdAndResetCode(long userId, String resetCode);
}

