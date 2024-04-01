package com.example.server.services.products;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.server.data_transfer_object.products.ProductRequest;
import com.example.server.models.Products;

public interface ProductService {
    List<Products> getAllProducts();
    Optional<Products> getProduct(String id);
    void add(ProductRequest request,MultipartFile productImage) throws IOException, SQLException;
    void edit(ProductRequest request,MultipartFile productImage, String id) throws IOException, SQLException;
    void delete(String id);
    byte[] generateReport() throws IOException;
}
