package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.ResetCodeEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;

import java.util.UUID;

public interface ResetCodeRepository extends ActiveJpaRepository<ResetCodeEntity> {
	
	ResetCodeEntity findByUserIdAndResetCode(UUID userId, String resetCode);

	void deleteByUserId(UUID userId);
}

