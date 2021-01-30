package com.jworks.afro.pixels.service.controllers;

import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.models.ApiResponseDto;
import com.jworks.afro.pixels.service.models.EndUserImageCategoryDto;
import com.jworks.afro.pixels.service.services.impl.EndUserImageCategoryService;
import com.jworks.afro.pixels.service.utils.ApiUtil;
import com.jworks.afro.pixels.service.utils.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Johnpaul Chukwu.
 * @since 31/12/2020
 */



@RestController
@RequestMapping(
        value = RestConstants.API_PREFIX + "/afro-pixel/image-categories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class EndUserImageCategoryController {

    private final EndUserImageCategoryService endUserImageCategoryService;


    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllCategories(){

        List<EndUserImageCategoryDto> allCategories = endUserImageCategoryService.getAllCategories();
        return ApiUtil.retrievedMany("image category","image categories",allCategories);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createCategory(@Validated @RequestBody EndUserImageCategoryDto endUserImageCategoryDto) throws SystemServiceException {

        endUserImageCategoryService.createCategory(endUserImageCategoryDto);
        return ApiUtil.created("image category",endUserImageCategoryDto);
    }

    @PutMapping
    public ResponseEntity<ApiResponseDto> updateCategory(@Validated @RequestBody EndUserImageCategoryDto endUserImageCategoryDto,
                                                         @RequestParam Long categoryId) throws SystemServiceException, NotFoundRestApiException {

        endUserImageCategoryService.updateCategory(categoryId,endUserImageCategoryDto);

        String whatWasUpdated = String.format("category with name %s", endUserImageCategoryDto.getName());
        return ApiUtil.updated(whatWasUpdated,endUserImageCategoryDto);
    }
}
