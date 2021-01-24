package com.ysell.modules.synchronisation.models.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.ysell.modules.common.dto.LookupDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponseDto {

	private UUID id;

    private String name;

    private String description;

    private BigDecimal price; 		
    
    private int currentStock;

    private LookupDto organisation;

    private LookupDto productCategory;    
}
