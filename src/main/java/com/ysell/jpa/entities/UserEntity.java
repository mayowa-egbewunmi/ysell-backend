package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.entities.base.NamedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends ActiveAuditableEntity implements NamedEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
	private String email;

    @Column(nullable = false)
    private String hash;   

    @ManyToMany(fetch = FetchType.EAGER)
  /*  @JoinTable(
            name = "Users_Organisations",
            joinColumns = {@JoinColumn(name = "UserId")},
            inverseJoinColumns = {@JoinColumn(name = "OrganisationId")}
    )*/
    private Set<OrganisationEntity> organisations = new HashSet<>();
}
