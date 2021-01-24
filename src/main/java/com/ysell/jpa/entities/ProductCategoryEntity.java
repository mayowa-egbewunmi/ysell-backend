package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.entities.base.NamedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_categories")
public class ProductCategoryEntity extends ActiveAuditableEntity implements NamedEntity {

    @Column(nullable = false)
	private String name;
	
	private String description;

    @OneToMany(mappedBy = "productCategory")
    private Set<ProductEntity> products = new HashSet<>();
}
