package com.jworks.afro.pixels.service.repositories;

import com.jworks.afro.pixels.service.entities.EndUserImage;
import com.jworks.afro.pixels.service.entities.EndUserImageCategory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Repository
public interface EndUserImageRepository extends BaseRepository<EndUserImage,Long>{

    Optional<EndUserImage> findByNameAndEndUserImageCategory(String name, EndUserImageCategory endUserImageCategory);
}
