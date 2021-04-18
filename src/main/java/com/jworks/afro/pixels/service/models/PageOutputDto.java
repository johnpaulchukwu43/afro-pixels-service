package com.jworks.afro.pixels.service.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class PageOutputDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Serializable> elements;
    private int totalPages;
    private int currentPageNumber;

    public PageOutputDto(Page pageable) {
        this.elements = pageable.getContent();
        this.totalPages = pageable.getTotalPages();
        this.currentPageNumber = pageable.getNumber() + 1;
    }
}
