package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sales")
public class SaleEntity extends ActiveAuditableEntity {
	
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
}
