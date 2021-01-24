package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reset_codes")
public class ResetCodeEntity extends ActiveAuditableEntity {

	@Column(nullable = false)
	private String resetCode;

	@Column(nullable = false)
	private Date expiryTimestamp;
	
	@OneToOne
    @JoinColumn(nullable = false)    //name = "User_Id",
	private UserEntity user;
}
