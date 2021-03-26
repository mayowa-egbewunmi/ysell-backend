package com.ysell.jpa.entities;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.entities.enums.OrderStatus;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
@Where(clause = "is_active=1")
public class OrderEntity extends ActiveAuditableEntity {

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SaleEntity> sales = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private OrganisationEntity organisation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PaymentEntity> payments = new HashSet<>();


    public BigDecimal getTotalPrice() {
        return sales.stream().reduce(BigDecimal.ZERO, (sum, sale) -> sum.add(sale.getTotalSellingPrice()), BigDecimal::add);
    }


    public BigDecimal getAmountPaid() {
        return payments.stream().reduce(BigDecimal.ZERO, (sum, payment) -> sum.add(payment.getAmount()), BigDecimal::add);
    }


    public BigDecimal getAmountDue() {
        return getTotalPrice().subtract(getAmountPaid());
    }


    public boolean paymentComplete() {
        return getAmountPaid().compareTo(getTotalPrice()) >= 0;
    }
}
