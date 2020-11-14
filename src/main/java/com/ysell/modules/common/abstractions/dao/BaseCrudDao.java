package com.ysell.modules.common.abstractions.dao;

import com.ysell.jpa.entities.base.ActiveEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import com.ysell.modules.common.utilities.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BaseCrudDao<TEntity extends ActiveEntity, TId,  TCreateRequest, TUpdateRequest, TResponse>
        implements CrudDao<TId,  TCreateRequest, TUpdateRequest, TResponse> {

    private final ActiveJpaRepository<TEntity, TId> repo;
    private final Class<TEntity> entityClass;
    private final Class<TResponse> responseClass;
    private final ModelMapper mapper;

    @Override
    public List<TResponse> getAll() {
        return repo.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TResponse> getAllPaged(int page, int size) {
        return repo.findAll(PageRequest.of(page, size))
                .map(this::convertToResponse)
                .getContent();
    }


    @Override
    public TResponse get(TId id) {
        TEntity entity = getEntity(id);
        return convertToResponse(entity);
    }

    protected void beforeCreate(TCreateRequest request) {
    }

    @Override
    @Transactional
    public TResponse create(TCreateRequest request) {
        beforeCreate(request);

        TEntity entity = populateCreateEntity(request);
        entity = repo.save(entity);

        afterCreate(entity);

        return convertToResponse(entity);
    }

    protected TEntity populateCreateEntity(TCreateRequest request) {
        return populateInstance(request, entityClass);
    }

    protected void afterCreate(TEntity entity) {
    }

    protected void beforeUpdate(TId id, TUpdateRequest request) {
    }

    @Override
    @Transactional
    public TResponse update(TId id, TUpdateRequest request) {
        beforeUpdate(id, request);

        TEntity entity = getEntity(id);
        entity = populateUpdateEntity(request, entity);
        entity = repo.save(entity);

        afterUpdate(entity);

        return convertToResponse(entity);
    }

    protected TEntity populateUpdateEntity(TUpdateRequest request, TEntity entity) {
        return populate(request, entity);
    }

    protected void afterUpdate(TEntity entity) {
    }

    protected void beforeDelete(TId id) {
    }

    @Override
    @Transactional
    public TResponse delete(TId id) {
        beforeDelete(id);

        TEntity entity = getEntity(id);
        repo.delete(entity);

        afterDelete(entity);

        return convertToResponse(entity);
    }

    protected void afterDelete(TEntity entity) {
    }

    private TEntity getEntity(TId id) {
        return repo.findById(id)
                .map(entity -> {
                    if (entity.isActive())
                        throw ServiceUtils.inactiveException(entityClass.getSimpleName(), id);
                    return entity;
                })
                .orElseThrow(() -> ServiceUtils.wrongIdException(entityClass.getSimpleName(), id));
    }

    protected TResponse convertToResponse(TEntity entity) {
        return populateInstance(entity, responseClass);
    }

    private <TFrom, T> T populateInstance(TFrom from, Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            T record = constructor.newInstance();
            return populate(from, record);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("%s does not contain a parameterless constructor", entityClass.getName()));
        }
    }

    private <TFrom, T> T populate(TFrom from, T to) {
        mapper.map(from, to);
        return to;
    }
}
