package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserVerificationLevel;
import com.jworks.afro.pixels.service.entities.VerificationLevel;
import com.jworks.afro.pixels.service.enums.Level;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.repositories.EndUserVerificationLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@Service
public class EndUserVerificationLevelService extends ServiceBluePrintImpl{


    private final EndUserVerificationLevelRepository endUserVerificationLevelRepository;
    private final VerificationLevelService verificationLevelService;

    @Autowired
    public EndUserVerificationLevelService(EndUserVerificationLevelRepository endUserVerificationLevelRepository, VerificationLevelService verificationLevelService) {
        super(endUserVerificationLevelRepository);
        this.endUserVerificationLevelRepository = endUserVerificationLevelRepository;
        this.verificationLevelService = verificationLevelService;
    }


    public EndUserVerificationLevel saveVerificationLevelAsNewlyOnBoardedEndUser(EndUser endUser) throws SystemServiceException {

        VerificationLevel verificationLevel = verificationLevelService.getVerificationLevelByLevel(Level.ONE);

        EndUserVerificationLevel endUserVerificationLevel = EndUserVerificationLevel.builder()
                .endUser(endUser)
                .verificationLevel(verificationLevel)
                .build();

        return endUserVerificationLevelRepository.save(endUserVerificationLevel);
    }
}
