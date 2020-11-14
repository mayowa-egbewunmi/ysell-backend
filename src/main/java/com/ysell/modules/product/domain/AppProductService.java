package com.ysell.modules.product.domain;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.product.domain.abstractions.ProductDao;
import com.ysell.modules.product.domain.abstractions.ProductService;
import com.ysell.modules.product.models.request.CreateProductRequest;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.ProductsByOrganisationRequest;
import com.ysell.modules.product.models.request.UpdateProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppProductService implements ProductService {

	private final ProductDao productDao;

	@Override
	public List<ProductResponse> getAllProducts() {
		return productDao.getAllProducts();
	}

	@Override
	public ProductResponse getProduct(long id) {
		return productDao.getProduct(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Product", id));
	}

	@Override
	@Transactional
	public ProductResponse createProduct(@Valid CreateProductRequest request) {
		productDao.getProductByNameAndOganisation(request.getName(), request.getOrganisation().getId())
				.orElseThrow(() -> new YSellRuntimeException(String.format("Product With Name %s Already Exists For Organisation", request.getName())));

		validateOrganisationAndCategory(request.getOrganisation().getId(), request.getProductCategory().getId());

		return productDao.createProduct(request);
	}

	@Override
	@Transactional
	public ProductResponse updateProduct(@Valid UpdateProductRequest request) {
		productDao.getProduct(request.getId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("Product", request.getId()));

		validateOrganisationAndCategory(request.getOrganisation().getId(), request.getProductCategory().getId());

		productDao.getProductByNameAndOganisation(request.getName(), request.getOrganisation().getId())
				.ifPresent(product -> {
					if (product.getId() != request.getId())
						throw new YSellRuntimeException(String.format("Product With Name %s Already Exists For Organisation", request.getName()));
				});

		return productDao.updateProduct(request);
	}

	private void validateOrganisationAndCategory(long organisationId, long categoryId) {
		if (!productDao.hasOrganisation(organisationId))
			throw new YSellRuntimeException(String.format("Organisation with Id %s does not exist", organisationId));
		if (!productDao.hasCategory(categoryId))
			throw new YSellRuntimeException(String.format("Product Category with Id %s does not exist", categoryId));
	}

	@Override
	@Transactional
	public ProductResponse deleteProduct(long id) {
		ProductResponse product = productDao.getProduct(id)
				.orElseThrow(() -> ServiceUtils.wrongIdException("Product", id));

		productDao.deleteProduct(id);

		return product;
	}

	@Override
	public List<ProductResponse> getProductsByOrganisations(@Valid ProductsByOrganisationRequest request) {
		for (LookupDto organisation : request.getOrganisations()) {
			if (!productDao.hasOrganisation(organisation.getId()))
				throw ServiceUtils.wrongIdException("Organisation", organisation.getId());
		}

		return request.getOrganisations().stream()
				.flatMap(organisation -> productDao.getProductsByOrganisation(organisation.getId()).stream())
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductCategoryResponse> getProductCategories() {
		return productDao.getProductCategories();
	}

	@Override
	public ProductCategoryResponse createOrUpdateProductCategory(@Valid ProductCategoryRequest request) {
		return productDao.saveProductCategory(request);
	}
}
