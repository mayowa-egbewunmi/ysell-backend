package com.ysell.jpa.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Products")
public class ProductEntity extends AuditableEntity<Long> {

	String name;

    private String description;
    
    private int currentStock;
    
    private BigDecimal price; 		
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Organisation_Id", nullable = false)
    private OrganisationEntity organisation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productCategoryId", nullable = false)
    private ProductCategoryEntity productCategory;	
}
