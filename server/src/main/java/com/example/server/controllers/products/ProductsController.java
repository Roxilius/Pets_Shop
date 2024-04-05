package com.example.server.controllers.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.GenericResponse;
import com.example.server.data_transfer_object.PageResponse;
import com.example.server.data_transfer_object.products.ProductRequest;
import com.example.server.data_transfer_object.products.ProductResponse;
import com.example.server.services.products.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/products")
@RestController
@Tag(name = "products")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173/")
public class ProductsController {
    @Autowired
    ProductService productService;

    @GetMapping("/get-all-products")
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam int page, @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice
            ) {
        try {
            PageResponse<ProductResponse> response = productService.getAllProducts(name, category, page, 10, sortBy,
                    sortOrder, minPrice, maxPrice);
            return ResponseEntity.ok().body(GenericResponse.success(response, "Success Get All Product"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror(e.getMessage()));
        }
    }

    @GetMapping("/get-product/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable(value = "id") String id) {
        try {
            return ResponseEntity.ok()
                    .body(GenericResponse.success(productService.getProduct(id), "Success Get Product"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addProduct(ProductRequest request,
            @RequestParam("Product Image") MultipartFile file) {
        try {
            productService.add(request, file);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Success Add New Product"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @PutMapping(value = "/edit-product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> editProduct(@PathVariable(value = "id") String id, ProductRequest request,
            @RequestParam("Product Image") MultipartFile file) {
        try {
            productService.edit(request, file, id);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Success Edit Product"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @DeleteMapping("/delete-product/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") String id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok().body(GenericResponse.success(null, "Success Delete Product"));
        } catch (ResponseStatusException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.eror(e.getReason()));

        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }

    @GetMapping("/report")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> report(HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
            return ResponseEntity.ok(productService.generateReport());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.eror("Internal Server Error!"));
        }
    }
}