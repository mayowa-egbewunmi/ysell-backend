package com.ysell.modules.synchronisation.domain.abstraction;

import com.ysell.modules.synchronisation.models.request.SynchronisationRequest;
import com.ysell.modules.synchronisation.models.response.SynchronisationResponse;

public interface SynchronisationService {

	SynchronisationResponse synchroniseRecords(SynchronisationRequest request);
}
