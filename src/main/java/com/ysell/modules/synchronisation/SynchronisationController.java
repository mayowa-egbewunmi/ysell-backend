package com.ysell.modules.synchronisation;

import com.ysell.common.annotations.WrapResponse;
import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.synchronisation.domain.abstraction.SynchronisationService;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ControllerConstants.VERSION_URL + "/synchronisation")
@RequiredArgsConstructor
@WrapResponse
public class SynchronisationController {

	private final SynchronisationService syncService;

    @PostMapping("")
    @ResponseBody
    public SynchronisationResponse synchroniseRecords(@RequestBody SynchronisationRequest request) {
        return syncService.synchroniseRecords(request);
    }
}
