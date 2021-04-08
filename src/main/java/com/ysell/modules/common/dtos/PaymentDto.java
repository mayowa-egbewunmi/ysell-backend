package com.ysell.modules.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ysell.jpa.entities.base.ActiveClientAuditableEntity;
import com.ysell.jpa.entities.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author tchineke
 * @since 20 February, 2021
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PaymentDto extends ActiveClientAuditableEntity {

    private UUID id;

    private LookupDto order;

    private BigDecimal amount;

    private PaymentMode mode;

    private String narration;
}
