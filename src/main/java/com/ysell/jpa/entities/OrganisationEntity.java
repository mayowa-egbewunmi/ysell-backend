package com.ysell.jpa.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ysell.jpa.entities.base.AuditableEntity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Organisations", indexes = { @Index(columnList = "email", unique = true) })
public class OrganisationEntity extends AuditableEntity<Long> {

    private String email;
	
    private String name;
    
    private String address;
    
    private String logo; 

    @ManyToMany(mappedBy = "organisations", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY)
    private Set<OrderEntity> orders = new HashSet<>();
}
