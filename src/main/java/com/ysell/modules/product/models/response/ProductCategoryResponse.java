package com.ysell.modules.product.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCategoryResponse {

	private long id;

	private String name;
	
	private String description;
}
