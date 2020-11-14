package com.ysell.modules.stock.models.response;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockResponse {

	private long id;
	
	private LookupDto product;
	
	private int currentStock;
}
