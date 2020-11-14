package com.ysell.modules.common.abstractions.services;

import com.ysell.jpa.entities.base.ActiveEntity;
import com.ysell.modules.common.abstractions.dao.BaseCrudDao;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BaseCrudService<TEntity extends ActiveEntity, TId,  TCreateRequest, TUpdateRequest, TResponse>
        implements CrudService<TId,  TCreateRequest, TUpdateRequest, TResponse> {

    private final BaseCrudDao<TEntity, TId, TCreateRequest, TUpdateRequest, TResponse> dao;

    @Override
    public List<TResponse> getAll() {
        return dao.getAll();
    }

    @Override
    public List<TResponse> getAllPaged(int page, int size) {
        return dao.getAllPaged(page, size);
    }

    @Override
    public TResponse get(TId id) {
        return dao.get(id);
    }

    @Override
    public TResponse create(TCreateRequest createRequest) {
        return dao.create(createRequest);
    }

    @Override
    public TResponse update(TId id, TUpdateRequest updateRequest) {
        return dao.update(id, updateRequest);
    }

    @Override
    public TResponse delete(TId id) {
        return dao.delete(id);
    }
}
