package com.jworks.afro.pixels.service.controllers;

import com.jworks.afro.pixels.service.exceptions.BadRequestException;
import com.jworks.afro.pixels.service.exceptions.NotFoundRestApiException;
import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import com.jworks.afro.pixels.service.exceptions.UnProcessableOperationException;
import com.jworks.afro.pixels.service.models.*;
import com.jworks.afro.pixels.service.services.impl.EndUserImageSearchService;
import com.jworks.afro.pixels.service.services.impl.EndUserImageService;
import com.jworks.afro.pixels.service.utils.ApiUtil;
import com.jworks.afro.pixels.service.utils.HasAuthority;
import com.jworks.afro.pixels.service.utils.RestConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jworks.afro.pixels.service.models.EndUserImageSearchDto.SortBy.fromString;

/**
 * @author Johnpaul Chukwu.
 * @since 28/03/2021
 */

@RestController
@RequestMapping(
        value = RestConstants.API_V1_PREFIX + "/afro-pixel-images",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class EndUserImageController {

    private final EndUserImageService endUserImageService;
    private final EndUserImageSearchService endUserImageSearchService;


    @GetMapping("/{imageId}")
    public ResponseEntity<ApiResponseDto> getImageById(@PathVariable Long imageId) throws NotFoundRestApiException {

        EndUserImageDto endUserImage = endUserImageService.getEndUserImageById(imageId);
        return ApiUtil.retrievedOne("Image",endUserImage);
    }


    @PostMapping
    @PreAuthorize(HasAuthority.OF_USER_OR_ADMIN)
    public ResponseEntity<ApiResponseDto> createImage(@Valid  @RequestBody CreateEndUserImageDto createEndUserImageDto) throws NotFoundRestApiException, SystemServiceException, UnProcessableOperationException {
        String username = ApiUtil.getLoggedInUser();
        String userImage = endUserImageService.createEndUserImage(createEndUserImageDto, username);

        return ApiUtil.created(userImage);
    }


    @GetMapping("/my")
    @PreAuthorize(HasAuthority.OF_USER_OR_ADMIN)
    public ResponseEntity<ApiResponseDto> getImageBelongingToUser(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws SystemServiceException, NotFoundRestApiException {

        String username = ApiUtil.getLoggedInUser();
        final PageInput pageInput = new PageInput(pageNumber - 1, pageSize);

        PageOutput<EndUserImageDto> endUserImages = endUserImageService.getImageBelongingToUser(username, pageInput);

        return ApiUtil.response(
                HttpStatus.OK, ApiResponseDto.Status.success,"  Images fetched successfully.",
                endUserImages
        );

    }


    @GetMapping("/search-suggestion")
    public ResponseEntity<ApiResponseDto> getSearchSuggestions(@RequestParam(name = "searchTerm") String searchTerm){
        EndUserImageSearchSuggestionDto suggestions = endUserImageSearchService.fetchSuggestions(searchTerm);

        return ApiUtil.response(
                HttpStatus.OK, ApiResponseDto.Status.success," Image suggestions fetched successfully.",
                suggestions
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto> searchImages(
            @RequestParam(name = "searchTerm") String searchTerm,
            @RequestParam(name = "color", required = false) String color,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "width", required = false) Integer width,
            @RequestParam(name = "height", required = false) Integer height,
            @RequestParam(name = "fileFormat", required = false) String fileFormat,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) throws BadRequestException {

        EndUserImageSearchDto endUserImageSearchDto = buildEndUserImageSearchRequest(
                searchTerm, color, sortOrder, sortBy,
                category, width, height, fileFormat,
                pageNumber, pageSize
        );

        PageOutput<EndUserImageDto> imageResults = endUserImageSearchService.searchImages(endUserImageSearchDto);

        return ApiUtil.response(
                HttpStatus.OK, ApiResponseDto.Status.success," Images fetched successfully.",
                imageResults
        );
    }

    private EndUserImageSearchDto buildEndUserImageSearchRequest(
            String searchTerm,String color,String sortOrder,String sortBy,
            String category,Integer width,Integer height,String fileFormat, Integer pageNumber, Integer pageSize
    ) throws BadRequestException {

        EndUserImageSearchDto.SortOptions.SortOptionsBuilder sortOptionsBuilder = EndUserImageSearchDto.SortOptions.builder();
        if(StringUtils.isNotBlank(sortBy)) sortOptionsBuilder.sortBy(fromString(sortBy));
        if(StringUtils.isNotBlank(sortOrder)) sortOptionsBuilder.sortOrder(EndUserImageSearchDto.SortOptions.fromString(sortOrder));
        EndUserImageSearchDto.SortOptions sortOptions = sortOptionsBuilder.build();

        EndUserImageSearchDto.FilterOptions filterOptions = EndUserImageSearchDto.FilterOptions.builder()
                .category(category)
                .color(color)
                .width(width)
                .height(height)
                .fileFormat(fileFormat)
                .build();

        PageInput pageInput = new PageInput(pageNumber,pageSize);

        return EndUserImageSearchDto.builder()
                .searchTerm(searchTerm)
                .pageInput(pageInput)
                .sortOptions(sortOptions)
                .filterOptions(filterOptions)
                .build();

    }




}
