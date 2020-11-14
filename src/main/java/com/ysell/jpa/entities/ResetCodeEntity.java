package com.ysell.jpa.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ysell.jpa.entities.base.ActiveEntity;
import com.ysell.jpa.entities.base.AuditableEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ResetCodes")
public class ResetCodeEntity extends AuditableEntity<Long> implements ActiveEntity {
	
	private String resetCode;
	
	private Date expiryTimestamp;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id", nullable = false)
	private UserEntity user;
	
	@Column(name="is_active", nullable = false, columnDefinition = "tinyint(1) default 1")
    protected boolean active = true;
}
