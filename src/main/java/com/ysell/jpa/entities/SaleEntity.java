package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import com.ysell.jpa.entities.enums.SaleType;
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
@Table(name = "sales")
@Where(clause = "is_active=1")
public class SaleEntity extends ActiveClientAuditableEntity {
	
    @ManyToOne
    @JoinColumn(nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalSellingPrice;

    @Column(nullable = false)
    private BigDecimal totalCostPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleType saleType = SaleType.SALE;
}
