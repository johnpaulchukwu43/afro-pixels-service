package com.jworks.afro.pixels.service.controllers;

import com.jworks.afro.pixels.service.entities.EndUserImage;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.exceptions.UnProcessableOperationException;
import com.jworks.afro.pixels.service.models.*;
import com.jworks.afro.pixels.service.services.impl.EndUserImageService;
import com.jworks.afro.pixels.service.utils.ApiUtil;
import com.jworks.afro.pixels.service.utils.HasAuthority;
import com.jworks.afro.pixels.service.utils.MapperUtil;
import com.jworks.afro.pixels.service.utils.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Johnpaul Chukwu.
 * @since 28/03/2021
 */

@RestController
@RequestMapping(
        value = RestConstants.API_V1_PREFIX + "/afro-pixel-images",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class EndUserImageController {

    private final EndUserImageService endUserImageService;


    @GetMapping("/{imageId}")
    public ResponseEntity<ApiResponseDto> getImageById(@PathVariable Long imageId) throws NotFoundRestApiException {

        EndUserImageDto endUserImage = endUserImageService.getEndUserImageById(imageId);
        return ApiUtil.retrievedOne("Image",endUserImage);
    }


    @PostMapping
    @PreAuthorize(HasAuthority.OF_USER_OR_ADMIN)
    public ResponseEntity<ApiResponseDto> createImage(@Valid  @RequestBody CreateEndUserImageDto createEndUserImageDto) throws NotFoundRestApiException, SystemServiceException, UnProcessableOperationException {
        String username = ApiUtil.getLoggedInUser();
        String userImage = endUserImageService.createEndUserImage(createEndUserImageDto, username);

        return ApiUtil.created(userImage);
    }


    @GetMapping("/my")
    @PreAuthorize(HasAuthority.OF_USER_OR_ADMIN)
    public ResponseEntity<ApiResponseDto> getImageBelongingToUser(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws SystemServiceException, NotFoundRestApiException {

        String username = ApiUtil.getLoggedInUser();
        final PageInput pageInput = new PageInput(pageNumber - 1, pageSize);

        PageOutput<EndUserImageDto> endUserImages = endUserImageService.getImageBelongingToUser(username, pageInput);

        return ApiUtil.response(
                HttpStatus.OK, ApiResponseDto.Status.success,"  Images fetched successfully.",
                endUserImages
        );

    }

}
