package com.ysell.modules.orders.models.dto.output;

import java.math.BigDecimal;

import com.ysell.modules.common.models.LookupDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

	private long id;
	
	private String name;
	
	private LookupDto organisation;

	private int currentStock;

	private BigDecimal price;
}
