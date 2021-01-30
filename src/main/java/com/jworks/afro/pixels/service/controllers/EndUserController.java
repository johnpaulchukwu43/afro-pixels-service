package com.jworks.afro.pixels.service.controllers;

import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.models.ApiResponseDto;
import com.jworks.afro.pixels.service.models.EndUserDto;
import com.jworks.afro.pixels.service.models.PasswordResetDto;
import com.jworks.afro.pixels.service.services.impl.EndUserService;
import com.jworks.afro.pixels.service.utils.ApiUtil;
import com.jworks.afro.pixels.service.utils.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@RestController
@RequestMapping(
        value = RestConstants.API_PREFIX + "/user",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class EndUserController {


    private final EndUserService endUserService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> createEndUser(@Valid @RequestBody EndUserDto endUserDto) throws SystemServiceException, NotFoundRestApiException {

        endUserService.performSignUpProcess(endUserDto);

        return ApiUtil.created("user",endUserDto);
    }

    @PutMapping
    public ResponseEntity<ApiResponseDto> updateUser(@Valid @RequestBody EndUserDto endUserDto) throws SystemServiceException, NotFoundRestApiException {

        String userRef = ApiUtil.getClientId();

        endUserService.updateEndUserDetails(endUserDto,userRef);

        String whatWasUpdated = String.format("user with ref %s", userRef);

        return ApiUtil.updated(whatWasUpdated,endUserDto);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponseDto> updateUserPassword(@Valid @RequestBody PasswordResetDto passwordResetDto) throws SystemServiceException, NotFoundRestApiException {
        String userRef = ApiUtil.getClientId();

        endUserService.resetUserPassword(passwordResetDto,userRef);

        String whatWasUpdated = String.format("password for user with ref: %s was updated.", userRef);

        return ApiUtil.updated(whatWasUpdated);

    }
}
