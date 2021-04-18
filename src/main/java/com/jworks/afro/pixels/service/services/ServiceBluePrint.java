package com.jworks.afro.pixels.service.services;

import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.models.ConditionType;
import com.jworks.afro.pixels.service.models.OrderPreference;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */
public interface ServiceBluePrint<T,K> {

    T save(T var1) throws SystemServiceException;

    T update(T var1) throws SystemServiceException;

    void delete(T var1) throws SystemServiceException;

    List<T> findAll() throws SystemServiceException;

    List<T> findAllOrdered(String var1, OrderPreference var2) throws SystemServiceException;

    Page<T> findAll(int var1, int var2) throws SystemServiceException;

    Page<T> findAllOrdered(int var1, int var2, String var3, OrderPreference var4) throws SystemServiceException;

    T findByParam(String var1, String var2) throws SystemServiceException;

    <V> T findByParams(Map<String, V> var1, ConditionType var2) throws SystemServiceException;

    T findByParam(String var1, long var2) throws SystemServiceException;

    List<T> findAsListByParam(String var1, String var2) throws SystemServiceException;

    List<T> findAsListByParam(String var1, long var2) throws SystemServiceException;

    List<T> findAsListByParamOrdered(String var1, String var2, String var3, OrderPreference var4) throws SystemServiceException;

    long getCount() throws SystemServiceException;

     K convertEntityToDto(T entity);
}
