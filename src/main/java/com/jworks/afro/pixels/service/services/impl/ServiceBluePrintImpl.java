package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.models.ConditionType;
import com.jworks.afro.pixels.service.models.OrderPreference;
import com.jworks.afro.pixels.service.repositories.BaseRepository;
import com.jworks.afro.pixels.service.services.ServiceBluePrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */
public class ServiceBluePrintImpl <T,K> implements ServiceBluePrint<T,K> {

    protected BaseRepository<T, Long> baseRepository;

    public ServiceBluePrintImpl(BaseRepository<T, Long> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public T save(T model) throws SystemServiceException {
        return this.baseRepository.save(model);
    }

    @Transactional
    public T update(T model) throws SystemServiceException {
        return this.baseRepository.save(model);
    }

    @Transactional
    public void delete(T model) throws SystemServiceException {
        this.baseRepository.delete(model);
    }


    public List<T> findAll() throws SystemServiceException {
        return this.baseRepository.findAll();
    }

    public Page<T> findAll(int pageNum, int pageSize) throws SystemServiceException {
        return this.baseRepository.findAll(new PageRequest(pageNum, pageSize));
    }

    public List<T> findAllOrdered(String orderParam,OrderPreference order) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Page<T> findAllOrdered(int page, int pageSize, String orderParam, OrderPreference order) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Transactional
    public T findByParam(String paramName, String paramVal) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Transactional
    public <V> T findByParams(Map<String, V> params, ConditionType conditionType) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Transactional
    public T findByParam(String paramName, long value) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Transactional
    public List<T> findAsListByParam(String paramName, String paramVal) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Transactional
    public List<T> findAsListByParam(String paramName, long value) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<T> findAsListByParamOrdered(String paramName, String paramVal, String orderParamName, OrderPreference order) throws SystemServiceException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public long getCount() throws SystemServiceException {
        return this.baseRepository.count();
    }

    @Override
    public K convertEntityToDto(T entity) {
        return null;
    }
}
