package com.jworks.afro.pixels.service.repositories;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserRole;
import com.jworks.app.commons.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Repository
public interface EndUserRoleRepository extends BaseRepository<EndUserRole,Long> {


    Optional<EndUserRole> findByEndUser(EndUser endUser);
}
