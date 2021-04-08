package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUserImageCategory;
import com.jworks.afro.pixels.service.exceptions.DuplicateEntryException;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.models.EndUserImageCategoryDto;
import com.jworks.afro.pixels.service.repositories.EndUserImageCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Johnpaul Chukwu.
 * @since 31/12/2020
 */

@Slf4j
@Service
public class EndUserImageCategoryService extends ServiceBluePrintImpl<EndUserImageCategory, EndUserImageCategoryDto> {

    private final EndUserImageCategoryRepository endUserImageCategoryRepository;

    @Autowired
    public EndUserImageCategoryService(EndUserImageCategoryRepository endUserImageCategoryRepository) {
        super(endUserImageCategoryRepository);
        this.endUserImageCategoryRepository = endUserImageCategoryRepository;
    }


    public void createCategory(EndUserImageCategoryDto endUserImageCategoryDto) throws SystemServiceException {

        String imageCategoryDtoName = endUserImageCategoryDto.getName();
        if (endUserImageCategoryRepository.findIdByName(imageCategoryDtoName).isPresent()) {
            throw new DuplicateEntryException(String.format("Image Category name: %s, already exists.", imageCategoryDtoName));
        }

        EndUserImageCategory endUserImageCategory = EndUserImageCategory.builder()
                .name(imageCategoryDtoName)
                .description(endUserImageCategoryDto.getDescription())
                .isActive(true)
                .build();

        super.save(endUserImageCategory);
    }

    public List<EndUserImageCategoryDto> getAllCategories(){
        return endUserImageCategoryRepository.findAll()
                .stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    public void updateCategory(Long categoryId,EndUserImageCategoryDto endUserImageCategoryDto) throws NotFoundRestApiException, SystemServiceException {

        EndUserImageCategory endUserImageCategory = getEndUserImageCategoryById(categoryId);

        BeanUtils.copyProperties(endUserImageCategoryDto,endUserImageCategory);

        super.save(endUserImageCategory);

    }

    public EndUserImageCategory getEndUserImageCategoryById(Long categoryId) throws NotFoundRestApiException {
        return endUserImageCategoryRepository.findById(categoryId).orElseThrow(
                    () -> new NotFoundRestApiException(String.format("Category with id: %s, not found.", categoryId))
            );
    }


    @Override
    public EndUserImageCategoryDto convertEntityToDto(EndUserImageCategory entity) {
        EndUserImageCategoryDto endUserImageCategoryDto = new EndUserImageCategoryDto();
        BeanUtils.copyProperties(entity,endUserImageCategoryDto);
        return endUserImageCategoryDto;
    }
}
