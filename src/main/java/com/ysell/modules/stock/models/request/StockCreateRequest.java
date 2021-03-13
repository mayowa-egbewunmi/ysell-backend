package com.ysell.modules.stock.models.request;

import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StockCreateRequest {

	@Valid
	private LookupDto product;

	@NotNull
	private Integer quantity;
}
