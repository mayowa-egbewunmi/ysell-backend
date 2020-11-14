package com.ysell.modules.synchronisation.models.dto.input;

import com.ysell.modules.common.models.ClientLookupDto;
import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleInputDto {

	@NotNull
	private UUID id;

	private ClientLookupDto order;

	@Valid
	private LookupDto product;

	@NotNull
    private Integer quantity;

    private double percentageDiscount;

    private BigDecimal totalPrice;

    @NotNull
    private Date clientCreatedAt;

    private Date clientUpdatedAt; 
}
