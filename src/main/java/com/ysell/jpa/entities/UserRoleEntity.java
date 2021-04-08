package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.AuditableEntity;
import com.ysell.jpa.entities.enums.Role;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_roles")
public class UserRoleEntity extends AuditableEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private Role role;
}
