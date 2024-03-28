package com.example.server.services.products;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.server.data_transfer_object.products.ProductRequest;
import com.example.server.models.Products;

public interface ProductService {
    List<Products> getAllProducts(); 
    ProductRequest add(ProductRequest request,MultipartFile productImage) throws IOException, SQLException;
}
