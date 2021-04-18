package com.jworks.afro.pixels.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Johnpaul Chukwu.
 * @since 15/04/2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    @NotBlank(message = "username is a required field")
    private String username;

    @NotBlank(message = "password is a required field")
    private String password;
}
