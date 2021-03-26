package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
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
@Table(name = "organisations")
@Where(clause = "is_active=1")
public class OrganisationEntity extends ActiveAuditableEntity implements NamedEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

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
