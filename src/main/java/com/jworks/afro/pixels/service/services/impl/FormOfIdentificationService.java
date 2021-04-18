package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.FormOfIdentification;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.repositories.FormOfIdentificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@Service
public class FormOfIdentificationService extends ServiceBluePrintImpl<FormOfIdentification,FormOfIdentification>{

    private final  FormOfIdentificationRepository formOfIdentificationRepository;

    @Autowired
    public FormOfIdentificationService(FormOfIdentificationRepository formOfIdentificationRepository) {
        super(formOfIdentificationRepository);
        this.formOfIdentificationRepository = formOfIdentificationRepository;
    }


    public FormOfIdentification verifyFormOfIdentificationType(String identificationType) throws NotFoundRestApiException {

        return formOfIdentificationRepository.findByName(identificationType).orElseThrow(()
                -> new NotFoundRestApiException(String.format("Form of identification type: %s, not recognized on system",identificationType)));
    }

}
