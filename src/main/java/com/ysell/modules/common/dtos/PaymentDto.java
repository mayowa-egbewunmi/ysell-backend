package com.ysell.modules.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PaymentDto extends ActiveAuditableEntity {

    private UUID id;

    private LookupDto order;

    private BigDecimal amount;

    private PaymentMode mode;

    private String narration;
}
