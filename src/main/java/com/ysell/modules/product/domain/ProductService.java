package com.ysell.modules.product.domain;

import com.ysell.modules.common.abstractions.CrudService;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.ProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductService extends CrudService<ProductRequest, ProductRequest, ProductResponse> {

	List<ProductResponse> getProductsByOrganisationIds(Set<UUID> organisationIds);

	List<ProductCategoryResponse> getProductCategories();

	ProductCategoryResponse createOrUpdateProductCategory(ProductCategoryRequest request);
}
