package com.ysell.modules.product.dependencies;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import com.ysell.jpa.entities.ProductCategoryEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductCategoryRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.modules.common.utilities.MapperUtils;
import com.ysell.modules.product.domain.abstractions.ProductDao;
import com.ysell.modules.product.models.request.CreateProductRequest;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.UpdateProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaProductDao implements ProductDao {

	private final ProductRepository productRepo;
	private final ProductCategoryRepository productCategoryRepo;
	private final OrganisationRepository orgRepo;
	private final ModelMapper mapper;

	@Override
	public List<ProductResponse> getAllProducts() {
		return MapperUtils.toStream(mapper, productRepo.findAll(), ProductResponse.class).collect(Collectors.toList());
	}

	@Override
	public Optional<ProductResponse> getProduct(long id) {
		return productRepo.findById(id)
		.map(productEntity -> mapper.map(productEntity, ProductResponse.class));
	}

	@Override
	public Optional<ProductResponse> getProductByNameAndOganisation(String name, long organisationId) {
		return productRepo.findByNameIgnoreCaseAndOrganisationId(name, organisationId)
				.map(productEntity -> mapper.map(productEntity, ProductResponse.class));
	}

	@Override
	public ProductResponse createProduct(CreateProductRequest request) {
		ProductEntity entity = mapper.map(request, ProductEntity.class);
		entity = productRepo.save(entity);

		ProductResponse response = mapper.map(entity, ProductResponse.class); 
		response.getOrganisation().setName(orgRepo.getOne(response.getOrganisation().getId()).getName());
		response.getProductCategory().setName(productCategoryRepo.getOne(response.getProductCategory().getId()).getName());
		
		return response;
	}

	@Override
	public ProductResponse updateProduct(UpdateProductRequest request) {
		ProductEntity originalEntity = productRepo.findById(request.getId()).orElse(null);
		
		ProductEntity entity = mapper.map(request, ProductEntity.class);
		entity.setCurrentStock(originalEntity.getCurrentStock());
		entity = productRepo.save(entity);
		
		return mapper.map(entity, ProductResponse.class); 
	}

	@Override
	public void deleteProduct(long id) {
		productRepo.deleteById(id);
	}
	
	@Override
	public boolean hasCategory(long categoryId) {
		ProductCategoryEntity categoryEntity = productCategoryRepo.findById(categoryId).orElse(null);
		return categoryEntity != null;
	}

	@Override
	public boolean hasOrganisation(long organisationId) {
		return orgRepo.findById(organisationId).orElse(null) != null;
	}

	@Override
	public List<ProductResponse> getProductsByOrganisation(long organisationId) {
		return MapperUtils.toStream(mapper, productRepo.findByOrganisationId(organisationId), ProductResponse.class).collect(Collectors.toList());
	}	

	@Override
	public List<ProductCategoryResponse> getProductCategories() {
		return MapperUtils.toStream(mapper, productCategoryRepo.findAll(), ProductCategoryResponse.class).collect(Collectors.toList());
	}

	@Override
	public ProductCategoryResponse saveProductCategory(ProductCategoryRequest request) {
		ProductCategoryEntity entity = mapper.map(request, ProductCategoryEntity.class);
		entity = productCategoryRepo.save(entity);
		
		return mapper.map(entity, ProductCategoryResponse.class);
	}
}
