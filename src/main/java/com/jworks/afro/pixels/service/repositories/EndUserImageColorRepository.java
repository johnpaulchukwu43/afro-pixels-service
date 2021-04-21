package com.jworks.afro.pixels.service.repositories;

import com.jworks.afro.pixels.service.entities.EndUserImage;
import com.jworks.afro.pixels.service.entities.EndUserImageColor;
import com.jworks.afro.pixels.service.entities.FormOfIdentification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Repository
public interface EndUserImageColorRepository extends BaseRepository<EndUserImageColor,Long>{

    Optional<EndUserImageColor> findByEndUserImage(EndUserImageColor endUserImageColor);
}
