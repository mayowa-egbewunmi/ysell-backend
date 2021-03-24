package com.ysell.jpa.repositories;

import com.ysell.jpa.entities.ResetCodeEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ResetCodeRepository extends ActiveJpaRepository<ResetCodeEntity> {
	
	ResetCodeEntity findByUserIdAndResetCode(UUID userId, String resetCode);

	@Modifying
	@Query("delete from ResetCodeEntity r where r.user.id = ?1")		//to enforce order of operations in hibernate
	void clearResetCodesForUser(UUID userId);
}