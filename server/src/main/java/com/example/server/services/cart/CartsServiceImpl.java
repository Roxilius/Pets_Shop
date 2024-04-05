package com.example.server.services.cart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.server.data_transfer_object.products.ProductResponse;
import com.example.server.data_transfer_object.user.CartRequest;
import com.example.server.data_transfer_object.user.CartResponse;
import com.example.server.models.Carts;
import com.example.server.models.Products;
import com.example.server.models.Users;
import com.example.server.repositorys.CartsRepository;
import com.example.server.repositorys.ProductsRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.image.ImageService;

@Service
public class CartsServiceImpl implements CartsService{
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    CartsRepository cartsRepository;
    @Autowired
    ImageService imageService;
    @Override
    public void add(CartRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());
        Products product = productsRepository.findProductsById(request.getProductId());
        Carts cart = cartsRepository.findByUsersAndProducts(user, product);
        if (cart == null) {
            Carts buffCart = new Carts();
            buffCart.setQuantity(request.getQuantity());
            buffCart.setUsers(user);
            buffCart.getProducts().add(product);
            cartsRepository.save(buffCart);
        } else{
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
            cartsRepository.save(cart);
        }
    }

    @Override
    public List<CartResponse> findAllCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());
        List<Carts> carts = cartsRepository.findByUsers(user);
        return carts.stream()
        .map(this::toCartResponse)
        .collect(Collectors.toList());
    }

    private CartResponse toCartResponse(Carts cart){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());

        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // String username = auth.getName();
        // Users users = userRepository.findUsersByUsername(username);
        List<ProductResponse> products = cart.getProducts().stream()
        .map(this::toProductResponse).collect(Collectors.toList());
        
        return CartResponse.builder()
        .id(cart.getId())
        .product(products)
        .quantity(cart.getQuantity())
        .username(user.getEmail())
        .build();
    }

    private ProductResponse toProductResponse(Products product){
        try {
            return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .stock(product.getStock())
            .image(imageService.convertImage(product.getImage()))
            .description(product.getDescription())
            .category(product.getCategory().getName())
            .build();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String id) {
        cartsRepository.deleteById(id);        
    }
}
