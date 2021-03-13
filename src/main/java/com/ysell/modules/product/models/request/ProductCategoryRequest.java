package com.ysell.modules.product.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductCategoryRequest {

	private UUID id;

	@NotNull
	private String name;

	private String description;
}
