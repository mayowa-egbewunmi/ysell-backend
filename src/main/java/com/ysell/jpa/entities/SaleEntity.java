package com.ysell.jpa.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ysell.jpa.entities.base.AuditableClientEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Sales")
public class SaleEntity extends AuditableClientEntity<Long> {
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Order_Id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Product_Id", nullable = false)
    private ProductEntity product;
    
    private int quantity;
    
    private double percentageDiscount;
    
    private BigDecimal totalPrice;
}
