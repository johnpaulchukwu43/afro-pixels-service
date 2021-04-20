package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jworks.afro.pixels.service.entities.EndUserImageColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MetaDataDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "imageUrl is a required field")
    private String imageUrl;

    @NotNull(message = "imageFileFormat is a required field")
    private String imageFileFormat;

    @NotNull(message = "imageWidth is a required field")
    private Integer imageWidth;

    @NotNull(message = "imageHeight is a required field")
    private Integer imageHeight;

    @NotNull(message = "imageColors is a required field")
    private List<EndUserImageColor> imageColors;
}
