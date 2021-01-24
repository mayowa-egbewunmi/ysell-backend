package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stocks")
public class StockEntity extends ActiveAuditableEntity {

    @ManyToOne
    @JoinColumn(nullable = false)   //name = "Product_Id",
    private ProductEntity product;

    @Column(nullable = false)
	private Integer quantity;
}
