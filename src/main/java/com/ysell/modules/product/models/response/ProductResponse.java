package com.ysell.modules.product.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("cost_price")
    private BigDecimal costPrice;

    @JsonProperty("selling_price")
    private BigDecimal sellingPrice;

    @JsonProperty("current_stock")
    private int currentStock;

    private LookupDto organisation;

    @JsonProperty("product_category")
    private LookupDto productCategory;


    public static ProductResponse from(ProductEntity entity, OrganisationEntity organisationEntity, ProductCategoryEntity productCategoryEntity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .costPrice(entity.getCostPrice())
                .sellingPrice(entity.getSellingPrice())
                .currentStock(entity.getCurrentStock())
                .organisation(LookupDto.create(organisationEntity))
                .productCategory(LookupDto.create(productCategoryEntity))
                .build();
    }
}
