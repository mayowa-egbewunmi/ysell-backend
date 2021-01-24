package com.ysell.modules.common.abstractions;

import com.ysell.modules.common.dto.PageWrapper;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CrudService<TCreateRequest, TUpdateRequest, TResponse> {

    List<TResponse> getAll();

    PageWrapper<TResponse> getAllPaged(Pageable pageable);

    PageWrapper<TResponse> getAllPaged(int page, int size, boolean isAscending, String... sortFields);

    TResponse getById(UUID id);

    TResponse create(TCreateRequest request);

    TResponse update(UUID id, TUpdateRequest request);

    TResponse delete(UUID id);
}