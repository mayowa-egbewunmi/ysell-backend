package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payments")
public class PaymentEntity extends ActiveAuditableEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMode mode;

    private String narration;
}
