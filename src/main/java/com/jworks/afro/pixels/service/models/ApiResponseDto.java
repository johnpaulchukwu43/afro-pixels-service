package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Johnpaul Chukwu.
 * @since 18/12/2020
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Status status;
    private String message;
    private Serializable data;

    private List<ResponseError> errors;

    public ApiResponseDto(Status status, String message, Serializable data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseDto{" + "status=" + status + ", message=" + message + ", data="+data+ "}";
    }

    public enum Status {
        success,
        fail,
        error,
        pending;
    }

    public static class ResponseError {
        String fieldName;
        String fieldError;

        public ResponseError(String fieldName, String fieldError) {
            this.fieldName = fieldName;
            this.fieldError = fieldError;
        }
    }
}
