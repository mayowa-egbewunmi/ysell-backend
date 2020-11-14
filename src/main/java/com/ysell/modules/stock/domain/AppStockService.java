package com.ysell.modules.stock.domain;

import com.ysell.modules.common.exceptions.YSellRuntimeException;
import com.ysell.modules.common.models.LookupDto;
import com.ysell.modules.common.utilities.ServiceUtils;
import com.ysell.modules.stock.domain.abstraction.StockDao;
import com.ysell.modules.stock.domain.abstraction.StockService;
import com.ysell.modules.stock.models.dto.ProductDto;
import com.ysell.modules.stock.models.request.StockDataRequest;
import com.ysell.modules.stock.models.request.StockRequest;
import com.ysell.modules.stock.models.response.StockDataResponse;
import com.ysell.modules.stock.models.response.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppStockService implements StockService {

	private final StockDao stockDao;

	@Override
	@Transactional
	public StockResponse updateStock(@Valid StockRequest request) {
		ProductDto productDto = stockDao.getProduct(request.getProduct().getId())
				.orElseThrow(() -> ServiceUtils.wrongIdException("Product", request.getProduct().getId()));

		int amountToRemove = request.getQuantity() * -1;
		if (amountToRemove > productDto.getCurrentStock())
			throw new YSellRuntimeException(String.format("Current Stock for %s is %d. Cannot remove %d", productDto.getName(), productDto.getCurrentStock(), amountToRemove));

		long stockId = stockDao.recordStockChange(request);
		productDto = stockDao.updateProductStock(request.getProduct().getId(), request.getQuantity());

		return new StockResponse(stockId, LookupDto.create(productDto.getId(), productDto.getName()), productDto.getCurrentStock());
	}

	@Override
	public List<StockDataResponse> getStockByDate(StockDataRequest request) {
		return stockDao.getStockByDate(request.getEarliestCreatedDate());
	}
}
