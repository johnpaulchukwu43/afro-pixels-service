package com.jworks.afro.pixels.service.controllers;

import com.jworks.afro.pixels.service.exceptions.DuplicateEntryException;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.models.ApiResponseDto;
import com.jworks.afro.pixels.service.models.CreateEndUserImageDto;
import com.jworks.afro.pixels.service.models.EndUserImageDto;
import com.jworks.afro.pixels.service.services.impl.EndUserImageService;
import com.jworks.afro.pixels.service.utils.ApiUtil;
import com.jworks.afro.pixels.service.utils.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Johnpaul Chukwu.
 * @since 28/03/2021
 */

@RestController
@RequestMapping(
        value = RestConstants.API_PREFIX + "/afro-pixel-images",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class EndUserImageController {

    private final EndUserImageService endUserImageService;


    @GetMapping
    public ResponseEntity<ApiResponseDto> getImageById(@RequestParam Long imageId) throws NotFoundRestApiException {

        EndUserImageDto endUserImage = endUserImageService.getEndUserImageById(imageId);
        return ApiUtil.retrievedOne("Image",endUserImage);
    }


    @PostMapping
    public ResponseEntity<ApiResponseDto> createImage(@Valid  @RequestBody CreateEndUserImageDto createEndUserImageDto) throws NotFoundRestApiException, DuplicateEntryException {
        //todo inspect token and get username of enduser
        String username = "default";
        String userImage = endUserImageService.createEndUserImage(createEndUserImageDto, username);

        return ApiUtil.created(userImage);
    }
}
