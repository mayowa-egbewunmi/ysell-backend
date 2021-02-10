package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reset_codes")
public class ResetCodeEntity extends ActiveAuditableEntity {

	@OneToOne
	@JoinColumn(nullable = false)
	private UserEntity user;

	@Column(nullable = false)
	private String resetCode;

	@Column(nullable = false)
	private Date expiryTimestamp;
}
