package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.*;
import com.jworks.afro.pixels.service.enums.Level;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.repositories.EndUserImageColorRepository;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@Service
public class EndUserImageColorService extends ServiceBluePrintImpl<EndUserImageColor,EndUserImageColor>{


    private final EndUserImageColorRepository endUserImageColorRepository;

    public EndUserImageColorService(EndUserImageColorRepository endUserImageColorRepository) {
        super(endUserImageColorRepository);
        this.endUserImageColorRepository = endUserImageColorRepository;
    }


    public void saveEndUserImageColor(EndUserImage endUserImage, String color) throws SystemServiceException {

        EndUserImageColor endUserImageColor = EndUserImageColor.builder()
                .endUserImage(endUserImage)
                .color(color)
                .build();

        save(endUserImageColor);
    }
}
