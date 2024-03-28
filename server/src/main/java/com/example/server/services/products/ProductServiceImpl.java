package com.example.server.services.products;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.products.ProductRequest;
import com.example.server.models.Products;
import com.example.server.repositorys.ProductsRepository;

import jakarta.transaction.Transactional;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductsRepository productsRepository;

    @Override
    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public ProductRequest add(ProductRequest request, MultipartFile productImage) throws IOException, SQLException{
        if (!productImage.getContentType().startsWith("image")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported File Type");
        }
        Products product = productsRepository.findByName(request.getName()).orElse(null);
        if (product == null) {
            Products newProduct = new Products();
            newProduct.setName(request.getName());
            newProduct.setCategory(request.getCategory());
            newProduct.setDescription(request.getCategory());
            newProduct.setPrice(request.getPrice());
            newProduct.setStock(request.getStock());
            newProduct.setImage(new SerialBlob(productImage.getBytes()));
            productsRepository.save(newProduct);
        } else{
            product.setDescription(request.getCategory());
            product.setStock(product.getStock() + request.getStock());
            productsRepository.save(product);
        }
        return request;
    }
}