package com.ysell.modules.synchronisation;

import com.ysell.modules.common.constants.ControllerConstants;
import com.ysell.modules.synchronisation.domain.SynchronisationService;
import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(SynchronisationController.PATH)
@RequiredArgsConstructor
public class SynchronisationController {

    public static final String PATH = ControllerConstants.VERSION_URL + "/synchronisation";

	private final SynchronisationService syncService;


    @PostMapping
    public SynchronisationResponse synchroniseRecords(@RequestBody @Valid SynchronisationRequest request) {
        return syncService.synchroniseRecords(request);
    }
}
