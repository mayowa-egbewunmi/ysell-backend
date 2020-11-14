package com.ysell.modules.stock.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

	private long id;
	
	private String name;

	private int currentStock;

	private BigDecimal price;
}
