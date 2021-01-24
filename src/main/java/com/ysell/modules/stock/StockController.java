package com.ysell.modules.stock;

import com.ysell.config.constants.AppConstants;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.common.dto.PageWrapper;
import com.ysell.modules.stock.domain.StockService;
import com.ysell.modules.stock.models.request.StockCreateRequest;
import com.ysell.modules.stock.models.response.StockResponse;
import com.ysell.modules.stock.models.response.StockCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping(StockController.PATH)
@RequiredArgsConstructor
public class StockController {

    public static final String PATH = ControllerConstants.VERSION_URL + "/stocks";
	
	private final StockService stockService;


    @PostMapping
    public StockCreateResponse postStock(@RequestBody @Valid StockCreateRequest request){
        return stockService.postStock(request);
    }


    @GetMapping("/by-date")
    public PageWrapper<StockResponse> getStockByDate(@RequestParam(required = false) LocalDate earliestCreatedDate,
                                                     @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) Pageable page){
        return stockService.getStockByDate(earliestCreatedDate, page);
    }
}
