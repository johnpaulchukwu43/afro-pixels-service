package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserImage;
import com.jworks.afro.pixels.service.entities.EndUserImageCategory;
import com.jworks.afro.pixels.service.entities.EndUserImageColor;
import com.jworks.afro.pixels.service.exceptions.DuplicateEntryException;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.exceptions.UnProcessableOperationException;
import com.jworks.afro.pixels.service.models.*;
import com.jworks.afro.pixels.service.repositories.EndUserImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
    private final EndUserImageColorService endUserImageColorService;


    @Autowired
    EndUserImageService(EndUserImageRepository endUserImageRepository, EndUserImageCategoryService endUserImageCategoryService, EndUserService endUserService, EndUserImageColorService endUserImageColorService){
        super(endUserImageRepository);
        this.endUserImageRepository = endUserImageRepository;
        this.endUserImageCategoryService = endUserImageCategoryService;
        this.endUserService = endUserService;
        this.endUserImageColorService = endUserImageColorService;
    }

    public EndUserImageDto getEndUserImageById(Long endUserImageId) throws NotFoundRestApiException {

        EndUserImage endUserImage = endUserImageRepository.findById(endUserImageId).orElseThrow(
                () -> new NotFoundRestApiException(String.format("Image with with id: %s, not found.", endUserImageId))
        );

        return convertEntityToDto(endUserImage);
    }

    @Transactional(rollbackOn = Exception.class)
    public String createEndUserImage(CreateEndUserImageDto createEndUserImageDto,String username) throws NotFoundRestApiException, SystemServiceException, UnProcessableOperationException {

        /*
        * 1. confirm the user exists for specified username
        * 2. confirm the image category exists for the category id specified.
        * 3. confirm the image is unique by name and category
        *
        * */

        EndUser endUser = getUserIfExists(username);

        checkIfUserCanUploadImage(endUser);

        EndUserImageCategory endUserImageCategory = getImageCategoryIfExists(createEndUserImageDto.getCategoryId());

        return persistEndUserImage(createEndUserImageDto, endUser, endUserImageCategory);
    }

    public PageOutput<EndUserImageDto> getImageBelongingToUser(String username, PageInput pageInput) throws NotFoundRestApiException {

        EndUser endUser = getUserIfExists(username);

        final PageRequest pageRequest = PageRequest.of(pageInput.getPage(), pageInput.getSize());

        Page<EndUserImage> endUserImages = endUserImageRepository.findByEndUser(endUser, pageRequest);

        List<EndUserImageDto> endUserImageDtoList = new ArrayList<>();

        endUserImages.getContent().forEach(endUserImage -> {
            EndUserImageDto endUserImageDto = convertEntityToDto(endUserImage);
            endUserImageDto.setImageOwner(null);
            endUserImageDtoList.add(endUserImageDto);
        });

        Page<EndUserImageDto> endUserImageDtoPage =
                new PageImpl<>(endUserImageDtoList,pageRequest,endUserImages.getTotalElements());


        return  PageOutput.fromPage(endUserImageDtoPage);
    }

    private void checkIfUserCanUploadImage(EndUser endUser) throws  UnProcessableOperationException {

        if(!endUser.isCanUploadImages() || !endUser.isActive()){
            throw new UnProcessableOperationException("User is not yet enabled to upload image. Contact Administrator.");
        }
    }


    private String persistEndUserImage(CreateEndUserImageDto createEndUserImageDto, EndUser endUser,EndUserImageCategory endUserImageCategory) throws SystemServiceException {

        String name = createEndUserImageDto.getName();
        checkIfImageExists(name,endUserImageCategory);

        EndUserImage.MetaData metaData = buildEndUserImageMetaData(createEndUserImageDto.getMetaData());

        EndUserImage endUserImageBuilder = EndUserImage.builder()
                .endUser(endUser)
                .endUserImageCategory(endUserImageCategory)
                .description(createEndUserImageDto.getDescription())
                .isActive(true)
                .name(name)
                .metaData(metaData)
                .tag(createEndUserImageDto.getTag())
                .build();

        EndUserImage persistedEndUserImage = endUserImageRepository.save(endUserImageBuilder);

        saveEndUserImageAndColors(persistedEndUserImage,createEndUserImageDto.getMetaData().getImageColors());

        return name;
    }

    private void saveEndUserImageAndColors(EndUserImage persistedEndUserImage, List<String> imageColors) throws SystemServiceException {
        for (String imageColor : imageColors) {
            endUserImageColorService.saveEndUserImageColor(persistedEndUserImage, imageColor.toLowerCase());
        }
    }

    private EndUserImage.MetaData buildEndUserImageMetaData(MetaDataDto metaDataDto) {

        EndUserImage.MetaData metaData = new EndUserImage.MetaData();

        BeanUtils.copyProperties(metaDataDto,metaData);

        return metaData;
    }

    private EndUserImageCategory getImageCategoryIfExists(Long categoryId) throws NotFoundRestApiException {

        return endUserImageCategoryService.getEndUserImageCategoryById(categoryId);
    }

    private EndUser getUserIfExists(String username) throws NotFoundRestApiException {

        return endUserService.getUserByUsername(username);
    }

    private void checkIfImageExists(String name, EndUserImageCategory endUserImageCategory) throws DuplicateEntryException {

        if(endUserImageRepository.findByNameAndEndUserImageCategory(name,endUserImageCategory).isPresent()){
            throw new DuplicateEntryException(String.format("User Image with name: %s already exists for category: %s", name,endUserImageCategory.getName()));
        }
    }


    @Override
    public EndUserImageDto convertEntityToDto(EndUserImage entity) {

        EndUserImageCategoryDto endUserImageCategoryDto =
                endUserImageCategoryService.convertEntityToDto(entity.getEndUserImageCategory());

        endUserImageCategoryDto.setDescription(null);

        EndUserDto endUserDto = EndUserDto.builder()
                                    .username(entity.getEndUser().getUsername())
                                    .build();

        EndUserImage.MetaData metaData = entity.getMetaData();
        MetaDataDto metaDataDto = new MetaDataDto();
        List<String> imageColorsDto = new ArrayList<>();

        BeanUtils.copyProperties(metaData,metaDataDto);
        List<EndUserImageColor> imageColors = metaData.getImageColors();

        imageColors.forEach(imageColor -> imageColorsDto.add(imageColor.getColor()));

        metaDataDto.setImageColors(imageColorsDto);


        return EndUserImageDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .metaData(metaDataDto)
                .endUserImageCategory(endUserImageCategoryDto)
                .imageOwner(endUserDto)
                .isActive(entity.isActive())
                .tag(entity.getTag())
                .build();
    }
}
