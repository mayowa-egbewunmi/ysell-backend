package com.ysell.modules.product.domain;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.ProductCategoryEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.repositories.OrganisationRepository;
import com.ysell.jpa.repositories.ProductCategoryRepository;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.modules.common.abstractions.BaseCrudService;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.product.models.request.ProductCategoryRequest;
import com.ysell.modules.product.models.request.ProductRequest;
import com.ysell.modules.product.models.response.ProductCategoryResponse;
import com.ysell.modules.product.models.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl
        extends BaseCrudService<ProductEntity, ProductRequest, ProductRequest, ProductResponse>
        implements ProductService {

    private final ProductRepository productRepo;

    private final ProductCategoryRepository productCategoryRepo;

    private final OrganisationRepository orgRepo;


    public ProductServiceImpl(ProductRepository productRepo, ProductCategoryRepository productCategoryRepo, OrganisationRepository orgRepo) {
        super(productRepo, ProductEntity.class, ProductResponse.class);

        this.productRepo = productRepo;
        this.productCategoryRepo = productCategoryRepo;
        this.orgRepo = orgRepo;
    }


    @Override
    public List<ProductResponse> getProductsByOrganisationIds(Set<UUID> organisationIds) {
        for (UUID organisationId : organisationIds) {
            if (!productRepo.existsById(organisationId))
                throw ServiceUtils.wrongIdException("Organisation", organisationId);
        }

        return productRepo.findByOrganisationIdIn(organisationIds).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductCategoryResponse> getProductCategories() {
        return productCategoryRepo.findAll().stream()
                .map(ProductCategoryResponse::from)
                .collect(Collectors.toList());
    }


    @Override
    public ProductCategoryResponse createOrUpdateProductCategory(ProductCategoryRequest request) {
        ProductCategoryEntity productCategoryEntity = productCategoryRepo.findById(request.getId())
                .orElse(new ProductCategoryEntity());

        productCategoryEntity.setName(request.getName());
        if (request.getDescription() != null)
            productCategoryEntity.setDescription(request.getDescription());

        productCategoryEntity = productCategoryRepo.save(productCategoryEntity);

        return ProductCategoryResponse.from(productCategoryEntity);
    }


    @Override
    protected void beforeCreate(ProductRequest request) {
        if (productRepo.existsByNameIgnoreCaseAndOrganisationId(request.getName(), request.getOrganisation().getId()))
            throw new YSellRuntimeException(String.format(
                    "Product With Name %s Already Exists For Organisation %s", request.getName(), request.getOrganisation().getId()
            ));

        validateOrganisationAndCategory(request.getOrganisation().getId(), request.getProductCategory().getId());
    }


    @Override
    protected void beforeUpdate(UUID productId, ProductRequest request) {
        productRepo.findFirstByNameIgnoreCaseAndOrganisationId(request.getName(), request.getOrganisation().getId()).ifPresent(productEntity -> {
            if (!productEntity.getId().equals(productId))
                throw new YSellRuntimeException(String.format(
                        "Product With Name %s Already Exists For Organisation %s", request.getName(), request.getOrganisation().getId()
                ));
        });

        validateOrganisationAndCategory(request.getOrganisation().getId(), request.getProductCategory().getId());
    }


    @Override
    protected ProductEntity populateUpdateEntity(ProductRequest productRequest, ProductEntity entity) {
        entity.setName(productRequest.getName());
        entity.setDescription(productRequest.getDescription());
        entity.setCostPrice(productRequest.getCostPrice());
        entity.setSellingPrice(productRequest.getSellingPrice());
        entity.setOrganisation(orgRepo.getOne(productRequest.getOrganisation().getId()));
        entity.setProductCategory(productCategoryRepo.getOne(productRequest.getProductCategory().getId()));

        return entity;
    }

    @Override
    protected ProductResponse convertToResponse(ProductEntity entity) {
        OrganisationEntity organisationEntity = orgRepo.getOne(entity.getOrganisation().getId());
        ProductCategoryEntity productCategoryEntity = entity.getProductCategory() == null ? null :
                productCategoryRepo.getOne(entity.getProductCategory().getId());

        return ProductResponse.from(entity, organisationEntity, productCategoryEntity);
    }


    private void validateOrganisationAndCategory(UUID organisationId, UUID categoryId) {
        if (!orgRepo.existsById(organisationId))
            ServiceUtils.throwWrongIdException("Organisation", organisationId);
        if (!productCategoryRepo.existsById(categoryId))
            ServiceUtils.throwWrongIdException("Product Category", categoryId);
    }
}
