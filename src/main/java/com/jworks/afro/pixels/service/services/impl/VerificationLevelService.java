package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.VerificationLevel;
import com.jworks.afro.pixels.service.enums.Level;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.repositories.VerificationLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@Service
public class VerificationLevelService extends ServiceBluePrintImpl<VerificationLevel,VerificationLevel>{


    private final VerificationLevelRepository verificationLevelRepository;

    @Autowired
    public VerificationLevelService(VerificationLevelRepository verificationLevelRepository) {
        super(verificationLevelRepository);
        this.verificationLevelRepository = verificationLevelRepository;
    }

    public VerificationLevel getVerificationLevelByLevel(Level level) throws SystemServiceException {

        return verificationLevelRepository.findByLevel(level).orElseThrow(() -> new SystemServiceException("System error, expected Level config not found."));
    }


}
