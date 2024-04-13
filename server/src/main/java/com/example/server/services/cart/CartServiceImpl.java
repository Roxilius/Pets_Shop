package com.example.server.services.cart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.data_transfer_object.products.ProductResponse;
import com.example.server.data_transfer_object.user.CartItemsResponse;
import com.example.server.data_transfer_object.user.CartRequest;
import com.example.server.data_transfer_object.user.CartResponse;
import com.example.server.models.Cart;
import com.example.server.models.CartItems;
import com.example.server.models.Products;
import com.example.server.models.Users;
import com.example.server.repositorys.CartItemsRepository;
import com.example.server.repositorys.CartRepository;
import com.example.server.repositorys.ProductsRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.image.ImageService;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemsRepository cartItemsRepository;
    @Autowired
    ImageService imageService;

    @Override
    public CartResponse findAllCart() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Users user = usersRepository.findUsersByEmail(auth.getName());
            Cart cart = cartRepository.findCartByUsers(user);
            List<CartItemsResponse> cartItemsResponse = cart.getCartItems().stream().map(this::toCartItemsResponse)
            .collect(Collectors.toList());
            Integer totalAmount = 0;
            for (CartItemsResponse cartItem : cartItemsResponse) {
                totalAmount += cartItem.getAmount();
            }
            CartResponse response = new CartResponse();
            response.setId(cart.getId());
            response.setEmail(user.getEmail());
            response.setTotalAmount(totalAmount);
            response.setCartItems(cartItemsResponse);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ProductResponse toProductResponse(Products product) {
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

    private CartItemsResponse toCartItemsResponse(CartItems cartItems) {
        CartItemsResponse response = new CartItemsResponse();
        response.setId(cartItems.getId());
        response.setQty(cartItems.getQty());
        response.setAmount(cartItems.getAmount());
        response.setProduct(toProductResponse(cartItems.getProduct()));
        return response;
    }

    @Override
    public void add(CartRequest request) {
        try {
            if (request.getQuantity() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Qty");
            }
            Products product = productsRepository.findProductsById(request.getProductId());
            if (product.getStock() < request.getQuantity()) {
                request.setQuantity(product.getStock());
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Users user = usersRepository.findUsersByEmail(auth.getName());

            Cart cart = cartRepository.findCartByUsers(user);
            if (cart == null) {
                Cart newCart = new Cart();
                newCart.setUsers(user);
                cartRepository.save(newCart);

                CartItems cartItems = new CartItems();
                cartItems.setQty(request.getQuantity());
                cartItems.setAmount(request.getQuantity() * product.getPrice());
                cartItems.setProduct(product);
                cartItemsRepository.save(cartItems);

                newCart.getCartItems().add(cartItems);
                cartRepository.save(newCart);
            } else {
                // jika product sudah ada di keranjang user
                Boolean same = false;
                Set<CartItems> cartItems = cart.getCartItems();
                for (CartItems items : cartItems) {
                    if (items.getProduct().equals(product)) {
                        same = true;
                        if ((request.getQuantity() + items.getQty()) > product.getStock()) {
                            items.setQty(product.getStock());
                        } else{
                            items.setQty(request.getQuantity() + items.getQty());
                        }
                        items.setAmount(product.getPrice() * items.getQty());
                        break;
                    }
                }
                if (same) {
                    cart.setCartItems(cartItems);
                    cartRepository.save(cart);
                } else{
                    // jika product tidak ada di keranjang user
                    CartItems buffCartItems = new CartItems();
                    buffCartItems.setQty(request.getQuantity());
                    buffCartItems.setAmount(request.getQuantity() * product.getPrice());
                    buffCartItems.setProduct(product);
                    cartItemsRepository.save(buffCartItems);
                    cart.getCartItems().add(buffCartItems);
                    cartRepository.save(cart);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Override
    // public void delete(String id) {
    // cartsRepository.deleteById(id);
    // }
}
