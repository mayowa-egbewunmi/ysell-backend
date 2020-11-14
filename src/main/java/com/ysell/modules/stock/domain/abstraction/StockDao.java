package com.ysell.modules.stock.domain.abstraction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ysell.modules.stock.models.dto.ProductDto;
import com.ysell.modules.stock.models.request.StockRequest;
import com.ysell.modules.stock.models.response.StockDataResponse;

public interface StockDao {
	
	long recordStockChange(StockRequest request);
	
	ProductDto updateProductStock(long productId, int quantity);
	
	Optional<ProductDto> getProduct(long productId);
	
	List<StockDataResponse> getStockByDate(Date earliestCreatedDate);
}
