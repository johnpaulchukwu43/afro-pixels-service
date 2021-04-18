package com.jworks.afro.pixels.service.models;

import com.jworks.afro.pixels.service.exceptions.SystemServiceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;


public class PageInput implements Serializable {

    private static final Sort ASCENDING_SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");

    private static final long serialVersionUID = 1L;

    /**
     * One-indexed page number.
     */
    private final int page;

    /**
     * Number of items in each page.
     */
    private final int size;

    public PageInput(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public static PageRequest toPageRequest(PageInput pageInput, int maxPageSize) throws SystemServiceException {
        return toPageRequest(pageInput, maxPageSize, true);
    }

    public static PageRequest toPageRequest(PageInput pageInput, int maxPageSize, boolean ascending) throws SystemServiceException {
        if (pageInput.getPage() < 1 || pageInput.getSize() < 1)
            throw new SystemServiceException("Invalid page or size.");
        if (pageInput.getSize() > maxPageSize)
            throw new SystemServiceException("Page size too large.");
        return PageRequest.of(pageInput.getPage() - 1, pageInput.getSize(), ascending ? ASCENDING_SORT_BY_ID : ASCENDING_SORT_BY_ID.descending());
    }
}
