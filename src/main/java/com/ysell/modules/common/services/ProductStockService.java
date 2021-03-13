package com.ysell.modules.common.services;

import com.ysell.modules.common.dtos.ProductDto;

import java.util.UUID;

/**
 * @author created by Tobenna
 * @since 24 January, 2021
 */
public interface ProductStockService {

    void validateProductAndStock(UUID organisationId, UUID productId, int quantity);

    ProductDto updateProductStock(UUID productId, int quantity);
}
