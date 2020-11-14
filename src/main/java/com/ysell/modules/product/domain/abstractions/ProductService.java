package com.ysell.modules.product.domain.abstractions;

import com.ysell.modules.product.models.request.CreateProductRequest;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.ProductsByOrganisationRequest;
import com.ysell.modules.product.models.request.UpdateProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;

import java.util.List;

public interface ProductService {

	List<ProductResponse> getAllProducts();

	ProductResponse getProduct(long id);

	ProductResponse createProduct(CreateProductRequest request);

	ProductResponse updateProduct(UpdateProductRequest request);

	ProductResponse deleteProduct(long id);

	List<ProductResponse> getProductsByOrganisations(ProductsByOrganisationRequest request);

	List<ProductCategoryResponse> getProductCategories();

	ProductCategoryResponse createOrUpdateProductCategory(ProductCategoryRequest request);
}
