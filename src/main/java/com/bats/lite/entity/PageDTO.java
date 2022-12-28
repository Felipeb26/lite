package com.bats.lite.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class PageDTO<E, R> {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private long numberOfElements;
    private boolean hasNext;
    private List<?> content;

    public PageDTO(Page<E> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.hasNext = page.hasNext();
        this.content = page.getContent();
    }

}