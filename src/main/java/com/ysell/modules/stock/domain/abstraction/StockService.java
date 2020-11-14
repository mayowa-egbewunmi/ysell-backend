package com.ysell.modules.stock.domain.abstraction;

import com.ysell.modules.stock.models.request.StockDataRequest;
import com.ysell.modules.stock.models.request.StockRequest;
import com.ysell.modules.stock.models.response.StockDataResponse;
import com.ysell.modules.stock.models.response.StockResponse;

import java.util.List;

public interface StockService {

	StockResponse updateStock (StockRequest request);

	List<StockDataResponse> getStockByDate(StockDataRequest request);
}
