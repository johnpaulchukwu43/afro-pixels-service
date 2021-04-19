package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class CreateEndUserImageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(min = 2, message = "image name must be at least 2 characters long.")
    @Size(max = 100, message = "image name cannot be more than 100 characters long.")
    private String name;

    @NotNull(message = " Catergory Id is a required field")
    private Long categoryId;

    private String tag;

    @NotNull(message = "Description is a required field")
    private String description;

    @NotBlank(message = "Image url is a required field")
    private String imageUrl;
}
