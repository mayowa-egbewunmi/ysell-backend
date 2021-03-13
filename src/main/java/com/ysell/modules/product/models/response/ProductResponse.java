package com.ysell.modules.product.models.response;

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
}
