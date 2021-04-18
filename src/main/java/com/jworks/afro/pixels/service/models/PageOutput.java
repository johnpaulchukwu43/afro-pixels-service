package com.jworks.afro.pixels.service.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.Serializable;
import java.util.List;

public class PageOutput<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<T> elements;
    private final int totalPages;
    private final int currentPageNumber;

    public PageOutput(List<T> elements, int totalPages, int currentPageNumber) {
        this.elements = elements;
        this.totalPages = totalPages;
        this.currentPageNumber = currentPageNumber;
    }

    public List<T> getElements() {
        return elements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public static <T> PageOutput<T> fromPage(Page<T> page) {
        return new PageOutput<>(page.getContent(), page.getTotalPages(), page.getNumber() + 1);
    }
    public static <T> PageOutput<T> fromList(List<T> list) {
        Page<T> page = new PageImpl<>(list);
        return fromPage(page);
    }
}
