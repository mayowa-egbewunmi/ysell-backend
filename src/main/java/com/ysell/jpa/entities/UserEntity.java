package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import com.ysell.jpa.entities.base.NamedEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@Where(clause = "is_active=1")
public class UserEntity extends ActiveClientAuditableEntity implements NamedEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
	private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String hash;

    private String bankName;

    private String accountNumber;

    private String accountName;

    private Boolean activated = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_organisations",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "organisation_id")}
    )
    private Set<OrganisationEntity> organisations = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserRoleEntity> userRoles = new HashSet<>();
}
