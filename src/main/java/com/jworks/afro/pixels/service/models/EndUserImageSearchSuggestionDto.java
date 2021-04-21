package com.jworks.afro.pixels.service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Johnpaul Chukwu.
 * @since 15/04/2021
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndUserImageSearchSuggestionDto implements Serializable {

   private List<ImageResults> imageResults;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
   public static class ImageResults{
       private Long id;
       private String name;
   }
}
