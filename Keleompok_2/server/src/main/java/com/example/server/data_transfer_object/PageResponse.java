package com.example.server.data_transfer_object;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PageResponse<T> {
    private int page;
    private int size;
    private long totalItem;
    private List<T> items;

    public static <T> PageResponse<T> success(List<T> items, int page, int size, long totalItem) {
        return PageResponse.<T>builder()
        .page(page)
        .size(size)
        .totalItem(totalItem)
        .items(items)
        .build();    
    }
}
