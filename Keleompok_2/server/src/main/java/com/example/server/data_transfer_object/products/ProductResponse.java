package com.example.server.data_transfer_object.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private Integer price;
    private Integer stock;
    private String description;
    private String image;
    private String category;
}
