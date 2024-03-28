package com.example.server.controllers.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.products.ProductRequest;
import com.example.server.services.Upload.UploadImageService;
import com.example.server.services.products.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/products")
@RestController
@Tag(name = "products")
public class ProductsController {
    @Autowired
    ProductService productService;
    @Autowired
    UploadImageService uploadImageService;
    
    @GetMapping("/get-all-products")
    public ResponseEntity<Object> getAll(){
        try{
            return ResponseEntity.ok().body(GenericResponse.success(productService.getAllProducts(),"Success Get All Product"));
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }

    @PostMapping(value="/add-product",
    consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addProduct(ProductRequest request ,@RequestParam("Product Image") MultipartFile file){
        try{
            return ResponseEntity.ok().body(GenericResponse.success(productService.add(request,file),"Success Add New Product"));
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }
}
