package com.ysell.modules.common.abstractions.services;

import java.util.List;

public interface CrudService<TId,  TCreateRequest, TUpdateRequest, TResponse> {

    List<TResponse> getAll();

    List<TResponse> getAllPaged(int page, int size);

    TResponse get(TId id);

    TResponse create(TCreateRequest request);

    TResponse update(TId id, TUpdateRequest request);

    TResponse delete(TId id);
}
