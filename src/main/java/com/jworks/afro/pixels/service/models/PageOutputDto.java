package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class PageOutputDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Serializable> elements;
    private int totalPages;
    private int currentPageNumber;

    public PageOutputDto() {
    }

    public PageOutputDto(Page pageable) {
        this.elements = pageable.getContent();
        this.totalPages = pageable.getTotalPages();
        this.currentPageNumber = pageable.getNumber() + 1;
    }
}
