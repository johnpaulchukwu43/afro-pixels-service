package com.jworks.afro.pixels.service.repositories;

import com.jworks.afro.pixels.service.entities.FormOfIdentification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Repository
public interface FormOfIdentificationRepository extends BaseRepository<FormOfIdentification,Long>{

    Optional<FormOfIdentification> findByName(String name);
}
