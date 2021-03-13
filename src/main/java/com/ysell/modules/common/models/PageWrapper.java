package com.ysell.modules.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Data
public class PageWrapper<T> {

    private List<T> content;
    private int number;
    private int size;
    private int numberOfElements;
    private int totalPages;
    private long totalElements;
    private boolean isLast;

    public static <T> PageWrapper<T> from(Page<T> page) {
        return new PageWrapper<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }
}
