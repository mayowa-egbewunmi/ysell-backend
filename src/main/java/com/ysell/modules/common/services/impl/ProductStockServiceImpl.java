package com.ysell.modules.common.services.impl;

import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.modules.common.dto.ProductDto;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.ProductStockService;
import com.ysell.modules.common.utilities.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author created by Tobenna
 * @since 24 January, 2021
 */
@Service
@RequiredArgsConstructor
public class ProductStockServiceImpl implements ProductStockService {

    private final ProductRepository productRepo;


    @Override
    public void validateProductAndStock(UUID organisationId, UUID productId, int quantity) {
        ProductEntity productEntity = productRepo.findById(productId)
                .orElseThrow(() -> ServiceUtils.wrongIdException("Product", productId));

        if (quantity < 0)
            throw new YSellRuntimeException(String.format("Cannot order for negative (%d) amount of %s", quantity, productEntity.getName()));
        else if (productEntity.getOrganisation().getId() != organisationId)
            throw new YSellRuntimeException(String.format("Product with id %s does not belong to Organisation with id %s", productId, organisationId));
        else if (productEntity.getCurrentStock() < quantity)
            throw new YSellRuntimeException(String.format("%s has only %s items left. Cannot order %s", productEntity.getName(), productEntity.getCurrentStock(), quantity));
    }


    @Override
    public ProductDto updateProductStock(UUID productId, int quantity) {
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> ServiceUtils.wrongIdException("Product", productId));
        product.setCurrentStock(product.getCurrentStock() + quantity);

        ProductEntity productEntity = productRepo.save(product);

        return new ModelMapper().map(productEntity, ProductDto.class);
    }
}
