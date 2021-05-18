package com.jworks.afro.pixels.service.repositories;

import com.jworks.afro.pixels.service.entities.EndUserImageCategory;
import com.jworks.afro.pixels.service.repositories.projection.IdProjection;
import com.jworks.app.commons.repositories.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Repository
public interface EndUserImageCategoryRepository extends BaseRepository<EndUserImageCategory,Long> {

    Optional<IdProjection> findIdByName(String categoryName);
}
