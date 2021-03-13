package com.ysell.modules.common.abstractions;

import com.ysell.modules.common.response.PageWrapper;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CrudService<TCreateRequest, TUpdateRequest, TResponse> {

    List<TResponse> getAll();

    PageWrapper<TResponse> getByPage(Pageable pageable);

    PageWrapper<TResponse> getByPage(int page, int size, boolean isAscending, String... sortFields);

    TResponse getById(UUID id);

    TResponse create(TCreateRequest request);

    TResponse update(UUID id, TUpdateRequest request);

    TResponse delete(UUID id);
}