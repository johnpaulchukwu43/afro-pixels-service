package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class EndUserImageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private EndUserImageCategoryDto endUserImageCategory;

    private EndUserDto imageOwner;

    private String tag;

    private String description;

    private String imageUrl;

    private boolean isActive;

    private MetaDataDto metaData;
}
