package com.ysell.modules.product.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductCategoryRequest {

	private long id;

	private String name;
	
	private String description;
}
