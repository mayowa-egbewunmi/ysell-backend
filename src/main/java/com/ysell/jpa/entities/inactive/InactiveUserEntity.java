package com.ysell.jpa.entities.inactive;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tchineke
 * @since 25 March, 2021
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@Where( clause = "is_active<>1")
public class InactiveUserEntity extends ActiveAuditableEntity {

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
}