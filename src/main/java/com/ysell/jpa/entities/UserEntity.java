package com.ysell.jpa.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.ysell.jpa.entities.base.ActiveEntity;
import com.ysell.jpa.entities.base.AuditableEntity;

import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Users", indexes = { @Index(columnList = "email", unique = true) })
public class UserEntity extends AuditableEntity<Long> implements ActiveEntity {
	
	private String email;
	
    private String name;

    private String hash;   

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Users_Organisations",
            joinColumns = {@JoinColumn(name = "UserId")},
            inverseJoinColumns = {@JoinColumn(name = "OrganisationId")}
    )
    private Set<OrganisationEntity> organisations = new HashSet<>();
	
	@Column(name="is_active", nullable = false, columnDefinition = "tinyint(1) default 1")
    protected boolean active = true;
}
