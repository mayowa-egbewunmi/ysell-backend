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
@Table(name = "product_categories")
@Where(clause = "is_active=1")
public class ProductCategoryEntity extends ActiveAuditableEntity implements NamedEntity {

    @Column(nullable = false, unique = true)
	private String name;
	
	private String description;

    @OneToMany(mappedBy = "productCategory")
    private Set<ProductEntity> products = new HashSet<>();
}
