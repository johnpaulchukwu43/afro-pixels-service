package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserRole;
import com.jworks.afro.pixels.service.repositories.EndUserRoleRepository;
import com.jworks.app.commons.exceptions.NotFoundRestApiException;
import com.jworks.app.commons.services.impl.ServiceBluePrintImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@Service
public class EndUserRoleService extends ServiceBluePrintImpl<EndUserRole,EndUserRole> {

    private final EndUserRoleRepository endUserRoleRepository;

    @Autowired
    public EndUserRoleService(EndUserRoleRepository endUserRoleRepository) {
        super(endUserRoleRepository);
        this.endUserRoleRepository = endUserRoleRepository;
    }


    public EndUserRole getEndUserRole(EndUser endUser) throws NotFoundRestApiException {

       return endUserRoleRepository.findByEndUser(endUser).orElseThrow(() -> new NotFoundRestApiException(String.format("EndUserRole not found for endUser with username: %s", endUser.getUsername())));
    }

}
