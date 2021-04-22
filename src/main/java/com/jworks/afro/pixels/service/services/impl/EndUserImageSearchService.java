package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUserImage;
import com.jworks.afro.pixels.service.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EndUserImageSearchService {

    private final EntityManager entityManager;

    private final EndUserImageService endUserImageService;

    public EndUserImageSearchSuggestionDto fetchSuggestions(String searchTerm){
        log.info("searching image suggestions with name: {}",searchTerm);

        SearchSession searchSession = Search.session(entityManager);

        SearchResult<EndUserImage> searchResults = searchSession.search(EndUserImage.class)
                .where(endUserImage -> endUserImage.wildcard().field("name").matching(searchTerm+"*"))
                .fetch(10);

        List<EndUserImageSearchSuggestionDto.ImageResults> imageResults = new ArrayList<>();

        searchResults.hits().forEach(searchResult -> {
            EndUserImageSearchSuggestionDto.ImageResults imageResult = EndUserImageSearchSuggestionDto.ImageResults.builder()
                    .id(searchResult.getId())
                    .name(searchResult.getName())
                    .build();
            imageResults.add(imageResult);
        });

        return EndUserImageSearchSuggestionDto.builder()
                .imageResults(imageResults)
                .build();
    }

    public PageOutput<EndUserImageDto> searchImages(EndUserImageSearchDto endUserImageSearchDto){
        log.info("searching for image with name: {}",endUserImageSearchDto);

        String searchTerm = endUserImageSearchDto.getSearchTerm();
        EndUserImageSearchDto.FilterOptions filterOptions = endUserImageSearchDto.getFilterOptions();
        EndUserImageSearchDto.SortOptions sortOptions = endUserImageSearchDto.getSortOptions();
        PageInput pageInput = endUserImageSearchDto.getPageInput();

        String category = filterOptions.getCategory();
        String color = filterOptions.getColor();
        Integer height = filterOptions.getHeight();
        Integer width = filterOptions.getWidth();
        String fileFormat = filterOptions.getFileFormat();

        EndUserImageSearchDto.SortBy sortBy = sortOptions.getSortBy();
        SortOrder sortOrder = sortOptions.getSortOrder();

        Integer offset = pageInput.getPage() * pageInput.getSize();

        SearchSession searchSession = Search.session(entityManager);

        SearchResult<EndUserImage> searchResults = searchSession.search(EndUserImage.class)
                .where(endUserImage -> endUserImage.bool(booleanPredicate -> {


                    booleanPredicate.must(endUserImage.wildcard().fields("name","description").matching(searchTerm+"*"));

                    if(StringUtils.isNotBlank(category)){
                       booleanPredicate.must(endUserImage.match().field("endUserImageCategory.name").matching(category));
                    }

                    if(StringUtils.isNotBlank(color)){
                        booleanPredicate.must(endUserImage.range().field("metaData.imageColors.color").atLeast(color));
                    }

                    if(height != null){
                        booleanPredicate.must(endUserImage.range().field("metaData.imageHeight").atLeast(height));
                    }

                    if(width != null){
                        booleanPredicate.must(endUserImage.range().field("metaData.imageWidth").atLeast(width));
                    }

                    if(StringUtils.isNotBlank(fileFormat)){
                        booleanPredicate.must(endUserImage.match().field("metaData.imageFileFormat").matching(category));
                    }
                }))
                .sort(endUserImage -> endUserImage.composite(compositeSortComponentsStep -> {

                    if(sortBy != null){
                        compositeSortComponentsStep.add(
                                endUserImage.field(getIndexFieldNameFromSortOption(sortBy)).order(sortOrder)
                        );
                    }

                }))
                .fetch(offset, pageInput.getSize());

        List<EndUserImageDto> searchResultsDto = new ArrayList<>();

        searchResults.hits().forEach(endUserImage -> searchResultsDto.add(toImageDto(endUserImage)));

        return new PageOutput<>(searchResultsDto, pageInput.getPage(), searchResults.hits().size());

    }


    private String getIndexFieldNameFromSortOption(EndUserImageSearchDto.SortBy sortBy){

        switch (sortBy){
            case ID:
                return "_id";
            case DATE_CREATED:
                return "createdAt";
            default:
                log.error("Unknown sortBy option: {}, defaulting to id",sortBy);
                return "_id";
        }
    }

    EndUserImageDto toImageDto(EndUserImage endUserImage){
        return endUserImageService.convertEntityToDto(endUserImage);
    }
}
