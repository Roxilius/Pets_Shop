package com.example.server.data_access_object;

import com.example.server.data_transfer_object.PageResponse;
import com.example.server.models.Category;
import com.example.server.models.Products;

public interface ProductDao {
    PageResponse<Products> getAll(String name, Category category, int page, int size, String sortBy, String sortOrder, Integer minPrice, Integer maxPrice);
}
