package com.ysell.modules.stock.domain;

import com.ysell.modules.common.dto.PageWrapper;
import com.ysell.modules.stock.models.request.StockCreateRequest;
import com.ysell.modules.stock.models.response.StockCreateResponse;
import com.ysell.modules.stock.models.response.StockResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface StockService {

	StockCreateResponse postStock(StockCreateRequest request);

	PageWrapper<StockResponse> getStockByDate(LocalDate earliestCreatedDate, Pageable pageable);
}
