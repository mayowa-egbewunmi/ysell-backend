package com.ysell.modules.product.models.response;

import com.ysell.modules.common.models.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponse {

	private long id;

    private String name;

    private String description;

    private BigDecimal price; 		
    
    private int currentStock;

    private LookupDto organisation;

    private LookupDto productCategory;    
}
