package com.ysell.modules.stock.domain;

import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.entities.StockEntity;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.jpa.repositories.StockRepository;
import com.ysell.modules.common.dto.LookupDto;
import com.ysell.modules.common.dto.PageWrapper;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.services.ProductStockService;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.stock.models.request.StockCreateRequest;
import com.ysell.modules.stock.models.response.StockCreateResponse;
import com.ysell.modules.stock.models.response.StockResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AppStockService implements StockService {

	private final StockRepository stockRepo;

	private final ProductRepository productRepo;

	private final ProductStockService productStockService;

	private final ModelMapper mapper = new ModelMapper();


	@Override
	public StockCreateResponse postStock(StockCreateRequest request) {
		ProductEntity productEntity = productRepo.findById(request.getProduct().getId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("Product", request.getProduct().getId()));

		int amountToRemove = request.getQuantity() * -1;
		if (amountToRemove > productEntity.getCurrentStock())
			throw new YSellRuntimeException(String.format("Current Stock for %s is %d. Cannot remove %d", productEntity.getName(), productEntity.getCurrentStock(), amountToRemove));

		StockEntity stockEntity = recordStock(request);
		productStockService.updateProductStock(productEntity.getId(), request.getQuantity());

		return new StockCreateResponse(stockEntity.getId(),
				LookupDto.create(productEntity),
				stockEntity.getQuantity(),
				productEntity.getCurrentStock());
	}


	private StockEntity recordStock(StockCreateRequest request) {
		StockEntity stock = mapper.map(request, StockEntity.class);
		return stockRepo.save(stock);
	}


	private ProductEntity updateProductStock(ProductEntity productEntity, int quantity) {
		int newStockQuantity = productEntity.getCurrentStock() + quantity;
		productEntity.setCurrentStock(newStockQuantity);
		return productRepo.save(productEntity);
	}


	@Override
	public PageWrapper<StockResponse> getStockByDate(LocalDate earliestCreatedDate, Pageable pageable) {
		earliestCreatedDate = earliestCreatedDate == null ? LocalDate.MIN : earliestCreatedDate;

		Page<StockResponse> stocks = stockRepo.findByCreatedAtGreaterThanEqual(earliestCreatedDate, pageable)
				.map(stockEntity -> mapper.map(stockEntity, StockResponse.class));

		return PageWrapper.from(stocks);
	}
}
