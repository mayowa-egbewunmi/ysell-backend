package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reset_codes")
@Where(clause = "is_active=1")
public class ResetCodeEntity extends ActiveClientAuditableEntity {

	@OneToOne
	@JoinColumn(nullable = false)
	private UserEntity user;

	@Column(nullable = false)
	private String resetCode;

	@Column(nullable = false)
	private Date expiryTimestamp;
}
