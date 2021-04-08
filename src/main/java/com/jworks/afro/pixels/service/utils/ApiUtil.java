package com.jworks.afro.pixels.service.utils;


import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.models.ApiResponseDto;
import com.jworks.afro.pixels.service.models.PageOutputDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author efe ariaroo
 */
public class ApiUtil {

    public static String getClientId() throws SystemServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;

        String clientId = oAuth2Authentication.getOAuth2Request().getClientId();

        if(StringUtils.isBlank(clientId)){
            throw new SystemServiceException("Unable to retrieve user reference Id");
        }

        return clientId;

    }

    ApiUtil() { }

    
    public static void validate(BindingResult fields) throws SystemServiceException {
        if (fields.hasErrors()) {
            final FieldError fieldError = fields.getFieldError();
            if (fieldError != null) {
                throw new SystemServiceException("field errors: " + fieldError);
            }
        }
    }

    public static String getPrincipalName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static List<String> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public static ResponseEntity<ApiResponseDto> created(String whatWasCreated, Serializable data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto(ApiResponseDto.Status.success, createdPhrase(whatWasCreated), data));
    }

    public static ResponseEntity<ApiResponseDto> created(String whatWasCreated) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto(ApiResponseDto.Status.success, createdPhrase(whatWasCreated), null));
    }

    public static ResponseEntity<ApiResponseDto> updated(String whatWasUpdated, Serializable data) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto(ApiResponseDto.Status.success, updatedPhrase(whatWasUpdated), data));
    }

    public static ResponseEntity<ApiResponseDto> updated(String whatWasUpdated) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto(ApiResponseDto.Status.success, updatedPhrase(whatWasUpdated),null));
    }

    public static ResponseEntity<ApiResponseDto> response(HttpStatus httpStatus, ApiResponseDto.Status status, String message) {
        return response(httpStatus, status, message, null);
    }

    public static ResponseEntity<ApiResponseDto> response(HttpStatus httpStatus, ApiResponseDto.Status status, String message, Object data) {
        return ResponseEntity.status(httpStatus).body(new ApiResponseDto(status, message, (Serializable) data));
    }

    public static ResponseEntity<ApiResponseDto> retrievedOne(String name, Serializable dto) {
        return response(HttpStatus.OK, ApiResponseDto.Status.success, ApiUtil.retrievedPhrase(name), dto);
    }

    public static ResponseEntity<ApiResponseDto> retrievedMany(String singular, String plural, List<? extends Serializable> dtos) {
        return response(HttpStatus.OK, ApiResponseDto.Status.success, describeList(dtos, singular, plural), dtos);
    }

    public static ResponseEntity<ApiResponseDto> retrievedMany(String singular, String plural, PageOutputDto pageOutput) {
        return response(HttpStatus.OK, ApiResponseDto.Status.success, describeList(pageOutput.getElements(), singular, plural), pageOutput);
    }

    public static ResponseEntity<ApiResponseDto> retrievedMany(List<? extends Serializable> dtos) {
        return response(HttpStatus.OK, ApiResponseDto.Status.success, describeList(dtos), dtos);
    }

    static ResponseEntity<ApiResponseDto> retrievedMany(Map map) {
        return response(HttpStatus.OK, ApiResponseDto.Status.success, "Operation successful", map);
    }

    static ResponseEntity<ApiResponseDto> retrievedMany(PageOutputDto pageOutput) {
        return response(HttpStatus.OK, ApiResponseDto.Status.success, describeList(pageOutput.getElements()), pageOutput);
    }

    static ResponseEntity<ApiResponseDto> retrievedMany(Page page) {
        return retrievedMany(new PageOutputDto(page));
    }

    private static String createdPhrase(String what) {
        return String.format("Okay, %s was successfully created.", what);
    }

    private static String updatedPhrase(String what) {
        return String.format("Okay, %s was successfully updated.", what);
    }

    private static String retrievedPhrase(String what) {
        return String.format("Okay, %s was found.", what);
    }

    private static String describeList(List<? extends Serializable> dtos, String singular, String plural) {
        return String.format("Okay, %s %s found.",
                             dtos.isEmpty() ? "no"
                             : dtos.size() == 1 ? 1
                               : dtos.size(),
                             dtos.size() > 1 ? plural : singular);
    }


    private static String describeList(List<? extends Serializable> dtos) {
        return "Okay, records found.";
    }


}
