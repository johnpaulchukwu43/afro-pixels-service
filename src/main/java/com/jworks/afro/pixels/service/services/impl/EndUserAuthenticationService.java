package com.jworks.afro.pixels.service.services.impl;
import com.jworks.afro.pixels.service.utils.JwtUtil;
import com.jworks.app.commons.exceptions.NotFoundRestApiException;
import com.jworks.app.commons.models.AuthenticationRequest;
import com.jworks.app.commons.models.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author Johnpaul Chukwu.
 * @since 16/04/2021
 */

@Service
@RequiredArgsConstructor
public class EndUserAuthenticationService {

    private final EndUserService endUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) throws NotFoundRestApiException {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( authenticationRequest.getUsername(),authenticationRequest.getPassword())
        );

        UserDetails userDetails = endUserService.loadUserByUsername(authenticationRequest.getUsername());
        return new AuthenticationResponse(jwtUtil.generateToken(userDetails));
    }
}
