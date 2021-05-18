package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jworks.app.commons.validator.AcceptedPasswordFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Johnpaul Chukwu.
 * @since 17/12/2020
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CreateEndUserDto implements Serializable {

    private String firstName;

    private String lastName;

    @NotBlank(message = "Email Address is a required field")
//    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$",message="must be a valid email address.")
    private String emailAddress;

    @AcceptedPasswordFormat
    @NotBlank(message = "password is a required field")
    private String password;

    @NotBlank(message = "username is a required field")
    @Size(min = 3, max = 50, message = "username must be between 3 - 50 characters long")
    private String username;

    private String phoneNumber;

    private String formOfIdentificationType;

    private String formOfIdentificationDocumentUrl;
}
