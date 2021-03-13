package com.ysell.modules.product.models.response;

import com.ysell.jpa.entities.ProductCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductCategoryResponse {

	private UUID id;

	private String name;
	
	private String description;


	public static ProductCategoryResponse from(ProductCategoryEntity productCategoryEntity) {
		return new ProductCategoryResponse(
				productCategoryEntity.getId(),
				productCategoryEntity.getName(),
				productCategoryEntity.getDescription()
		);
	}
}
