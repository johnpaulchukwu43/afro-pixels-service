package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserImage;
import com.jworks.afro.pixels.service.entities.EndUserImageCategory;
import com.jworks.afro.pixels.service.exceptions.DuplicateEntryException;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.models.CreateEndUserImageDto;
import com.jworks.afro.pixels.service.models.EndUserImageDto;
import com.jworks.afro.pixels.service.repositories.EndUserImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 28/03/2021
 */

@Slf4j
@Service
public class EndUserImageService extends ServiceBluePrintImpl<EndUserImage, EndUserImageDto> {

    private final EndUserImageRepository endUserImageRepository;
    private final EndUserImageCategoryService endUserImageCategoryService;
    private final EndUserService endUserService;


    @Autowired
    EndUserImageService(EndUserImageRepository endUserImageRepository, EndUserImageCategoryService endUserImageCategoryService, EndUserService endUserService){
        super(endUserImageRepository);
        this.endUserImageRepository = endUserImageRepository;
        this.endUserImageCategoryService = endUserImageCategoryService;
        this.endUserService = endUserService;
    }


    public EndUserImageDto getEndUserImageById(Long endUserImageId) throws NotFoundRestApiException {

        EndUserImage endUserImage = endUserImageRepository.findById(endUserImageId).orElseThrow(
                () -> new NotFoundRestApiException(String.format("Image with with id: %s, not found.", endUserImageId))
        );

        return convertEntityToDto(endUserImage);
    }

    private void checkIfImageExists(String name, EndUserImageCategory endUserImageCategory) throws DuplicateEntryException {

        if(endUserImageRepository.findByNameAndEndUserImageCategory(name,endUserImageCategory).isPresent()){
            throw new DuplicateEntryException(String.format("User Image with name: %s already exists for category: %s", name,endUserImageCategory.getName()));
        }
    }

    public String createEndUserImage(CreateEndUserImageDto createEndUserImageDto,String username) throws NotFoundRestApiException, DuplicateEntryException {

        /*
        * 1. confirm the user exists for specified username
        * 2. confirm the image category exists for the category id specified.
        * 3. confirm the image is unique by name and category
        *
        * */

        EndUser endUser = getUserIfExists(username);

        EndUserImageCategory endUserImageCategory = getImageCategoryIfExists(createEndUserImageDto.getCategoryId());

        persistEndUserImage(createEndUserImageDto,endUser,endUserImageCategory);
        return username;
    }


    private String persistEndUserImage(CreateEndUserImageDto createEndUserImageDto, EndUser endUser,EndUserImageCategory endUserImageCategory) throws DuplicateEntryException {

        String name = createEndUserImageDto.getName();
        checkIfImageExists(name,endUserImageCategory);

        EndUserImage endUserImage = EndUserImage.builder()
                .endUser(endUser)
                .endUserImageCategory(endUserImageCategory)
                .description(createEndUserImageDto.getDescription())
                .imageUrl(createEndUserImageDto.getImageUrl())
                .isActive(false)
                .name(name)
                .tag(createEndUserImageDto.getTag())
                .build();

        endUserImageRepository.save(endUserImage);

        return name;
    }

    private EndUserImageCategory getImageCategoryIfExists(Long categoryId) throws NotFoundRestApiException {

        return endUserImageCategoryService.getEndUserImageCategoryById(categoryId);
    }

    private EndUser getUserIfExists(String username) throws NotFoundRestApiException {

        return endUserService.getUserByUsername(username);
    }




    @Override
    public EndUserImageDto convertEntityToDto(EndUserImage entity) {
        return EndUserImageDto.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .endUserImageCategory(entity.getEndUserImageCategory())
                .isActive(entity.isActive())
                .tag(entity.getTag())
                .build();
    }
}
