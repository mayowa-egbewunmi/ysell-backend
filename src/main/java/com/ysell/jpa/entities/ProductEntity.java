package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.entities.base.NamedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity extends ActiveAuditableEntity implements NamedEntity {

    @Column(nullable = false)
	private String name;

    private String description;
    
    private Integer currentStock;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @ManyToOne
    @JoinColumn(nullable = false)    //name = "organisation_Id"
    private OrganisationEntity organisation;

    @ManyToOne
    @JoinColumn(nullable = false)   //name = "product_category_Id"
    private ProductCategoryEntity productCategory;	
}
