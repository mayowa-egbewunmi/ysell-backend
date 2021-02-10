package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.entities.base.NamedEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity extends ActiveAuditableEntity implements NamedEntity {

    @Column(nullable = false, unique = true)
	private String name;

    private String description;

    @Column(nullable = false)
    private Integer currentStock;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @ManyToOne
    @JoinColumn(nullable = false)
    private OrganisationEntity organisation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductCategoryEntity productCategory;	
}
