package com.jworks.afro.pixels.service.repositories;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.repositories.projection.IdProjection;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Repository
public interface EndUserRepository extends BaseRepository<EndUser,Long>{

    Optional<IdProjection> findIdByUserReference(String userReference);

    Optional<EndUser> findByUserReference(String userReference);

    Optional<EndUser> findByUsername(String userReference);
    Optional<EndUser> findById(Long userId);

    Optional<IdProjection> findIdByUsername(String username);
    Optional<IdProjection> findIdByUsernameAndIdNot(String username, Long id);
    Optional<IdProjection> findIdByPhoneNumber(String phoneNumber);
    Optional<IdProjection> findIdByPhoneNumberAndIdNot(String phoneNumber, Long id);
    Optional<IdProjection> findIdByEmailAddress(String emailAddress);
    Optional<IdProjection> findIdByEmailAddressAndIdNot(String emailAddress, Long id);
}
