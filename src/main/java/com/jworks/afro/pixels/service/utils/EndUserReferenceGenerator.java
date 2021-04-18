package com.jworks.afro.pixels.service.utils;

import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.repositories.EndUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EndUserReferenceGenerator {

    private final EndUserRepository endUserRepository;


    public String generateReference() throws SystemServiceException {
        int maxTrials = 10;
        String userRef;
        do {
            userRef = UUID.randomUUID().toString();
        } while (endUserRepository.findIdByUserReference(userRef).isPresent() && --maxTrials > 0);
        if (maxTrials <= 0)
            throw new SystemServiceException("Unable to generate unique client id");
        return userRef;
    }
}
