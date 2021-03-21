package com.ysell.modules.product.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductCategoryRequest {

	private UUID id;

	@NotEmpty
	private String name;

	private String description;
}
