package com.ysell.modules.product.domain.abstractions;

import java.util.List;
import java.util.Optional;

import com.ysell.modules.product.models.request.CreateProductRequest;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.UpdateProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;

public interface ProductDao {

	List<ProductResponse> getAllProducts();
	
	Optional<ProductResponse> getProduct(long id);
	
	Optional<ProductResponse> getProductByNameAndOganisation(String name, long organisationId);
	
	ProductResponse createProduct(CreateProductRequest request);
	
	ProductResponse updateProduct(UpdateProductRequest request);
	
	void deleteProduct(long id);
	
	boolean hasCategory(long categoryId);

	boolean hasOrganisation(long organisationId);

	List<ProductResponse> getProductsByOrganisation(long organisationId);
	
	List<ProductCategoryResponse> getProductCategories();

	ProductCategoryResponse saveProductCategory(ProductCategoryRequest request);
}
