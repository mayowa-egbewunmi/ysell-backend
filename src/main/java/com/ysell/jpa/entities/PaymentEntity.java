package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.*;
import org.hibernate.annotations.Where;

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
@Where(clause = "is_active=1")
public class PaymentEntity extends ActiveClientAuditableEntity {

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
