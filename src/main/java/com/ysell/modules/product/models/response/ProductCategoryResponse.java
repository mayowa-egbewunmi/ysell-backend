package com.ysell.modules.product.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductCategoryResponse {

	private UUID id;

	private String name;
	
	private String description;
}
