package com.ysell.modules.product.models.response;

import com.ysell.jpa.entities.OrganisationEntity;
import com.ysell.jpa.entities.ProductCategoryEntity;
import com.ysell.jpa.entities.ProductEntity;
import com.ysell.modules.common.dtos.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class ProductResponse {

	private UUID id;

    private String name;

    private String description;

    private BigDecimal price; 		
    
    private int currentStock;

    private LookupDto organisation;

    private LookupDto productCategory;


    public static ProductResponse from(ProductEntity entity, OrganisationEntity organisationEntity, ProductCategoryEntity productCategoryEntity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getSellingPrice())
                .currentStock(entity.getCurrentStock())
                .organisation(LookupDto.create(organisationEntity))
                .productCategory(LookupDto.create(productCategoryEntity))
                .build();
    }
}
