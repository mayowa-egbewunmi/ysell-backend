package com.ysell.modules.orders.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductDto {

	private UUID id;

	private String name;
	
	private LookupDto organisation;

	private int currentStock;
}
