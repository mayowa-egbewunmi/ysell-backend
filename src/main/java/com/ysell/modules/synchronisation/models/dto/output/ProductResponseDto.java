package com.ysell.modules.synchronisation.models.dto.output;

import java.math.BigDecimal;

import com.ysell.modules.common.models.LookupDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponseDto {

	private long id;

    private String name;

    private String description;

    private BigDecimal price; 		
    
    private int currentStock;

    private LookupDto organisation;

    private LookupDto productCategory;    
}
