package com.ysell.modules.stock.models.response;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class StockResponse {

	private UUID id;
	
	private LookupDto product;
	
	private int quantity;
}
