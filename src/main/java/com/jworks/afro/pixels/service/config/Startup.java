package com.jworks.afro.pixels.service.config;

import com.jworks.afro.pixels.service.services.impl.SearchIndexingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Startup implements ApplicationListener<ApplicationReadyEvent> {

    private final SearchIndexingService service;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            service.initiateIndexing();
        } catch (InterruptedException e) {
            log.error("Failed to reindex entities ",e);
        }
    }
}
