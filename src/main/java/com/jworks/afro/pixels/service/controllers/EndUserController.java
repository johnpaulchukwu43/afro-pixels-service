package com.jworks.afro.pixels.service.controllers;

import com.jworks.afro.pixels.service.models.*;
import com.jworks.afro.pixels.service.services.impl.EndUserAuthenticationService;
import com.jworks.afro.pixels.service.services.impl.EndUserService;
import com.jworks.afro.pixels.service.utils.HasAuthority;
import com.jworks.app.commons.exceptions.NotFoundRestApiException;
import com.jworks.app.commons.exceptions.SystemServiceException;
import com.jworks.app.commons.models.ApiResponseDto;
import com.jworks.app.commons.models.AuthenticationRequest;
import com.jworks.app.commons.models.AuthenticationResponse;
import com.jworks.app.commons.models.PasswordResetDto;
import com.jworks.app.commons.utils.ApiUtil;
import com.jworks.app.commons.utils.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@RestController
@RequestMapping(
        value = RestConstants.API_V1_PREFIX + "/user",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class EndUserController {


    private final EndUserService endUserService;
    private final EndUserAuthenticationService endUserAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> createEndUser(@Valid @RequestBody CreateEndUserDto endUserDto) throws SystemServiceException, NotFoundRestApiException {

        EndUserDto persistedUserInfo = endUserService.performSignUpProcess(endUserDto);

        return ApiUtil.created("user",persistedUserInfo);
    }

    @PutMapping("/{userId}")
    @PreAuthorize(HasAuthority.OF_USER_OR_ADMIN)
    public ResponseEntity<ApiResponseDto> updateUser(@Valid @RequestBody UpdateEndUserDto endUserDto ,@PathVariable Long userId) throws SystemServiceException, NotFoundRestApiException {

        String usernameOfLoggedInUser = ApiUtil.getLoggedInUser();

        endUserService.updateEndUserDetails(endUserDto,usernameOfLoggedInUser,userId);

        String whatWasUpdated = String.format("user with username %s", usernameOfLoggedInUser);

        return ApiUtil.updated(whatWasUpdated,endUserDto);
    }

    @PutMapping("/reset-password/{userId}")
    @PreAuthorize(HasAuthority.OF_USER_OR_ADMIN)
    public ResponseEntity<ApiResponseDto> updateUserPassword(@Valid @RequestBody PasswordResetDto passwordResetDto, @PathVariable Long userId) throws SystemServiceException, NotFoundRestApiException {
        String username = ApiUtil.getLoggedInUser();

        endUserService.resetUserPassword(passwordResetDto,username,userId);

        String whatWasUpdated = String.format("password for user with username: %s was updated.", username);

        return ApiUtil.updated(whatWasUpdated);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponseDto> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws NotFoundRestApiException {

        AuthenticationResponse authenticationResponse = endUserAuthenticationService.authenticateUser(authenticationRequest);
        return ApiUtil.authenticated(authenticationResponse);
    }
}
