package com.ysell.modules.common.abstractions;

import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import com.ysell.jpa.repositories.base.ActiveJpaRepository;
import com.ysell.modules.common.response.PageWrapper;
import com.ysell.modules.common.utilities.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;

@RequiredArgsConstructor
public class BaseCrudService<TEntity extends ActiveAuditableEntity, TCreateRequest, TUpdateRequest, TResponse>
        implements CrudService<TCreateRequest, TUpdateRequest, TResponse> {

    private final ActiveJpaRepository<TEntity> repo;

    private final Class<TEntity> entityClass;

    private final Class<TResponse> responseClass;


    @Override
    public List<TResponse> getAll() {
        return repo.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public PageWrapper<TResponse> getAllPaged(Pageable pageable) {
        return PageWrapper.from(
                repo.findAll(pageable)
                        .map(this::convertToResponse)
        );
    }


    @Override
    public PageWrapper<TResponse> getAllPaged(int page, int size, boolean isAscending, String... sortFields) {
        Direction direction = isAscending ? Direction.ASC : Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, by(direction, sortFields));

        return getAllPaged(pageable);
    }


    @Override
    public TResponse getById(UUID id) {
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


    protected void beforeUpdate(UUID id, TUpdateRequest request) {
    }


    @Override
    public TResponse update(UUID id, TUpdateRequest request) {
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


    protected void beforeDelete(UUID id) {
    }


    @Override
    public TResponse delete(UUID id) {
        beforeDelete(id);

        TEntity entity = getEntity(id);
        repo.delete(entity);

        afterDelete(entity);

        return convertToResponse(entity);
    }


    protected void afterDelete(TEntity entity) {
    }


    private TEntity getEntity(UUID id) {
        return repo.findById(id)
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
        new ModelMapper().map(from, to);
        return to;
    }
}
