package com.ysell.jpa.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ysell.jpa.entities.base.AuditableEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ProductCategories")
public class ProductCategoryEntity extends AuditableEntity<Long> {

	private String name;
	
	private String description;

    @OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();
}
