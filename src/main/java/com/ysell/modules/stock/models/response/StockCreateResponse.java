package com.ysell.modules.stock.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class StockCreateResponse {

	private UUID id;
	
	private LookupDto product;

	@JsonProperty("quantity_added")
	private int quantityAdded;

	@JsonProperty("product_total_stock")
	private int productTotalStock;
}
