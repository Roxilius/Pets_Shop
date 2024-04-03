package com.example.server.data_transfer_object.products;

import java.sql.Blob;


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
    private String price;
    private Integer stock;
    private String description;
    private Blob image;
    private String category;
}
