package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUserFormOfIdentification;
import com.jworks.afro.pixels.service.repositories.EndUserFormOfIdentificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@Service
public class EndUserFormOfIdentificationService extends ServiceBluePrintImpl<EndUserFormOfIdentification,EndUserFormOfIdentification>{

    private final EndUserFormOfIdentificationRepository endUserFormOfIdentificationRepository;

    @Autowired
    public EndUserFormOfIdentificationService(EndUserFormOfIdentificationRepository endUserFormOfIdentificationRepository) {
        super(endUserFormOfIdentificationRepository);
        this.endUserFormOfIdentificationRepository = endUserFormOfIdentificationRepository;
    }
}
