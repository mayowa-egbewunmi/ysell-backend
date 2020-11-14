package com.ysell.jpa.entities;

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
@Table(name = "Stocks")
public class StockEntity extends AuditableEntity<Long> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Product_Id", nullable = false)
    private ProductEntity product;

	private int quantity;
}
