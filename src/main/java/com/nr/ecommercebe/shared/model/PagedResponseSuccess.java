package com.nr.ecommercebe.shared.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Getter
public class PagedResponseSuccess<T> {
    static String status = "success";
    String message;
    List<T> items;
    Pagination pagination;

    public PagedResponseSuccess(String message, Page<T> page) {
        this.message = message;
        this.items = page.getContent();
        this.pagination = new Pagination(page);
    }

    @Getter
    @Setter
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class Pagination {
        int page;
        int size;
        long totalItems;
        int totalPages;
        boolean hasNext;
        boolean hasPrevious;

        public Pagination(Page<?> page) {
            this.page = page.getNumber();
            this.size = page.getSize();
            this.totalItems = page.getTotalElements();
            this.totalPages = page.getTotalPages();
            this.hasNext = page.hasNext();
            this.hasPrevious = page.hasPrevious();
        }
    }
}
