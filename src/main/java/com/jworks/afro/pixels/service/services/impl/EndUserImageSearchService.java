package com.jworks.afro.pixels.service.services.impl;

import com.jworks.afro.pixels.service.entities.EndUser;
import com.jworks.afro.pixels.service.entities.EndUserImage;
import com.jworks.afro.pixels.service.models.EndUserImageSearchSuggestionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.query.SearchResult;
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
}
