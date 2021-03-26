package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stocks")
@Where(clause = "is_active=1")
public class StockEntity extends ActiveAuditableEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
	private Integer quantity;
}
