package com.jworks.afro.pixels.service.repositories;

import com.jworks.afro.pixels.service.entities.VerificationLevel;
import com.jworks.afro.pixels.service.enums.Level;
import com.jworks.app.commons.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Repository
public interface VerificationLevelRepository extends BaseRepository<VerificationLevel,Long> {

    Optional<VerificationLevel> findByLevel(Level level);
}
