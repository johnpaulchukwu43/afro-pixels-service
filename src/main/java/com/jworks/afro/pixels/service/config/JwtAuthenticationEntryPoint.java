package com.jworks.afro.pixels.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jworks.afro.pixels.service.models.ApiResponseDto;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Johnpaul Chukwu.
 * @since 15/04/2021
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Exception exception = (Exception) request.getAttribute("exception");

        String message;

        if (exception != null) {

            if (exception.getCause() != null) {
                message = exception.getCause().toString() + " " + exception.getMessage();
            } else {
                message = exception.getMessage();
            }
        } else {

            if (authException.getCause() != null) {
                message = authException.getCause().toString() + " " + authException.getMessage();
            } else {
                message = authException.getMessage();
            }
        }

        ApiResponseDto responseDto = ApiResponseDto.builder()
                .message(message)
                .status(ApiResponseDto.Status.fail)
                .build();

        byte[] body = new ObjectMapper().writeValueAsBytes(responseDto);

        response.getOutputStream().write(body);
    }

}
