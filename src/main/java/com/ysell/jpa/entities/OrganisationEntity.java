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
@Table(name = "organisations")
public class OrganisationEntity extends ActiveAuditableEntity implements NamedEntity {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;
    
    private String logo; 

    @ManyToMany(mappedBy = "organisations")
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "organisation")
    private Set<ProductEntity> products = new HashSet<>();

    @OneToMany(mappedBy = "organisation")
    private Set<OrderEntity> orders = new HashSet<>();
}
