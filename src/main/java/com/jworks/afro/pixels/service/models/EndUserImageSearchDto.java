package com.jworks.afro.pixels.service.models;


import com.jworks.app.commons.exceptions.BadRequestException;
import com.jworks.app.commons.models.PageInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

import java.io.Serializable;

/**
 * @author Johnpaul Chukwu.
 * @since 15/04/2021
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndUserImageSearchDto implements Serializable {

    private String searchTerm;
    private PageInput pageInput;
    private SortOptions sortOptions;
    private FilterOptions filterOptions;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortOptions implements Serializable {

        private SortBy sortBy;
        private SortOrder sortOrder;

        public static SortOrder fromString(String term) throws BadRequestException {
            for (SortOrder sortOrder: SortOrder.values()) {
                if (sortOrder.name().equals(term)) {
                    return sortOrder;
                }
            }
            throw new BadRequestException(String.format("Unknown sortOrder: %s", term));
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilterOptions implements Serializable {

        private String color;
        private String category;
        private String fileFormat;
        private Integer width;
        private Integer height;
    }

    public enum SortBy {
        ID, DATE_CREATED,MOST_DOWNLOADS;

        public static SortBy fromString(String term) throws BadRequestException {
            for (SortBy sortBy: SortBy.values()) {
                if (sortBy.name().equals(term)) {
                    return sortBy;
                }
            }
            throw new BadRequestException(String.format("Unknown sortBy: %s", term));
        }
    }

}
