package com.example.server.services.products;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

import com.example.server.data_transfer_object.PageResponse;
import com.example.server.data_transfer_object.products.ProductRequest;
import com.example.server.data_transfer_object.products.ProductResponse;

public interface ProductService {
    PageResponse<ProductResponse> getAllProducts(String name, String category, int page, int size, String sortBy, String sortOrder);
    ProductResponse getProduct(String id);
    void add(ProductRequest request,MultipartFile productImage) throws IOException, SQLException;
    void edit(ProductRequest request,MultipartFile productImage, String id) throws IOException, SQLException;
    void delete(String id);
    byte[] generateReport() throws IOException;
}
