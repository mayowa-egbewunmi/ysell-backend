package com.ysell.modules.stock.models.response;

import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class StockCreateResponse {

	private UUID id;
	
	private LookupDto product;

	private int quantityAdded;

	private int productTotalStock;
}
