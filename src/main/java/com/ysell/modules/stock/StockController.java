package com.ysell.modules.stock;

import com.ysell.common.annotations.WrapResponse;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.stock.domain.abstraction.StockService;
import com.ysell.modules.stock.models.request.StockDataRequest;
import com.ysell.modules.stock.models.request.StockRequest;
import com.ysell.modules.stock.models.response.StockDataResponse;
import com.ysell.modules.stock.models.response.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ControllerConstants.VERSION_URL + "/stocks")
@RequiredArgsConstructor
@WrapResponse
public class StockController {
	
	private final StockService stockService;

    @PostMapping("")
    @ResponseBody
    public StockResponse updateStock(@RequestBody StockRequest request){
        return stockService.updateStock(request);
    }

    @PostMapping("by_date")
    @ResponseBody
    public List<StockDataResponse> getStockByDate(@RequestBody StockDataRequest request){
        return stockService.getStockByDate(request);
    }
}
