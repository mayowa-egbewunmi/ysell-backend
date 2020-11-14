package com.ysell.modules.stock.dependencies;

import com.ysell.jpa.entities.ProductEntity;
import com.ysell.jpa.entities.StockEntity;
import com.ysell.jpa.repositories.ProductRepository;
import com.ysell.jpa.repositories.StockRepository;
import com.ysell.modules.common.utilities.MapperUtils;
import com.ysell.modules.stock.domain.abstraction.StockDao;
import com.ysell.modules.stock.models.dto.ProductDto;
import com.ysell.modules.stock.models.request.StockRequest;
import com.ysell.modules.stock.models.response.StockDataResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaStockDao implements StockDao {

	private final StockRepository stockRepo;	
	private final ProductRepository productRepo;	
	private final ModelMapper mapper;

	@Override
	public long recordStockChange(StockRequest request) {
		StockEntity stock = mapper.map(request, StockEntity.class);
		stock.setQuantity(request.getQuantity());
		stock = stockRepo.save(stock);
		return stock.getId();
	}

	@Override
	public ProductDto updateProductStock(long productId, int quantity) {
		ProductEntity product = productRepo.findById(productId).orElse(null);	
		product.setCurrentStock(product.getCurrentStock() + quantity);
		product = productRepo.save(product);
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public Optional<ProductDto> getProduct(long productId) {
		return productRepo.findById(productId)
				.map(productEntity -> mapper.map(productEntity, ProductDto.class));
	}

	@Override
	public List<StockDataResponse> getStockByDate(Date earliestCreatedDate) {
		List<StockEntity> stocks =  earliestCreatedDate == null ? stockRepo.findAll() : 
			stockRepo.findByCreatedAtGreaterThanEqual(earliestCreatedDate, JpaSort.by(Direction.ASC, "CreatedBy"));			
		return MapperUtils.toStream(mapper, stocks, StockDataResponse.class).collect(Collectors.toList());
	}
}
