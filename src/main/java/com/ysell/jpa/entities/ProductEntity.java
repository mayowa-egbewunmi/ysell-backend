package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import com.ysell.jpa.entities.base.NamedEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
@Where(clause = "is_active=1")
public class ProductEntity extends ActiveClientAuditableEntity implements NamedEntity {

    @Column(nullable = false, unique = true)
	private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private Integer currentStock = 0;

    @ManyToOne
    @JoinColumn(nullable = false)
    private OrganisationEntity organisation;

    @ManyToOne
    private ProductCategoryEntity productCategory;	
}
