package com.jworks.afro.pixels.service.advice;

import com.jworks.afro.pixels.service.exceptions.*;
import com.jworks.afro.pixels.service.models.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.util.*;

/**
 * @author Johnpaul Chukwu.
 * @since 16/04/2021
 */

@Slf4j
@RestControllerAdvice
public class GeneralControllerAdvice {

    //todo handle http method not supported exception

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponseDto handleSystemError(Exception ex) {
        return processException(ApiResponseDto.Status.error, "System error. Please contact the administrator.", ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponseDto handleBadCredentialsException(BadCredentialsException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponseDto handleDisabledException(DisabledException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseDto handleTransactionServiceException(BadRequestException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponseDto handleGeneralDuplicateException(DuplicateEntryException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(NotFoundRestApiException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto handleEntityNotException(NotFoundRestApiException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(UnProcessableOperationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponseDto handleUnProcessableOperationException(UnProcessableOperationException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponseDto handleAccessDeniedException(AccessDeniedException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(ForbiddenUserUpdateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponseDto handleForbiddenUserUpdateException(ForbiddenUserUpdateException ex) {
        return processException(ApiResponseDto.Status.fail, ex.getMessage(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ApiResponseDto methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("An exception occurred :",ex);
        BindingResult result = ex.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponseDto handleDbConstraintViolation(DataIntegrityViolationException ex) {
        return processException(ApiResponseDto.Status.fail,"An existing record already exists", ex);
    }


    private ApiResponseDto processException(ApiResponseDto.Status status, String message, Exception ex){
        log.error(message,ex);
        return new ApiResponseDto(status, message, (Serializable) warn(ex));
    }

    private Map<String,String> warn(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        String errorId = UUID.randomUUID().toString().replace("-", "");
        log.warn(ex.getMessage() + " <errorId: " + errorId + ">", ex);
        errorMap.put("errorId",errorId);
        return errorMap;
    }

    private ApiResponseDto processFieldErrors(List<FieldError> fieldErrors) {
        List<ApiResponseDto.ResponseError> responseErrors = new ArrayList<>();
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            log.error("field error validation: {} ",fieldError);
            responseErrors.add(new ApiResponseDto.ResponseError(fieldError.getField(),fieldError.getDefaultMessage()));
        }
        return ApiResponseDto.builder()
                .status(ApiResponseDto.Status.fail)
                .message("validation error")
                .errors(responseErrors)
                .build();
    }

}
