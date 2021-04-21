package com.jworks.afro.pixels.service.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchIndexingService {

    private final EntityManager entityManager;

    @Value("${hibernate.search.backend.mass-index.number-of-threads}")
    private Integer numberOfThreads;

    @Transactional
    public void initiateIndexing() throws InterruptedException {
        log.info("Initiating indexing...");

        SearchSession searchSession = Search.session(entityManager);
        searchSession.massIndexer()
                .threadsToLoadObjects(numberOfThreads)
                .startAndWait();

        log.info("All entities indexed");
    }
}
