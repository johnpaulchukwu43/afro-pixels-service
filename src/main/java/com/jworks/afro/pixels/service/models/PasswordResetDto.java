package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jworks.afro.pixels.service.validator.AcceptedPasswordFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Johnpaul Chukwu.
 * @since 24/12/2020
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PasswordResetDto {

    @AcceptedPasswordFormat
    private String newPassword;
    @AcceptedPasswordFormat
    private String confirmPassword;
}
