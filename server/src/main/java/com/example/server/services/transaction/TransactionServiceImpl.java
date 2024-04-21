package com.example.server.services.transaction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import com.example.server.data_transfer_object.transaction.TransactionResponse;
import com.example.server.models.Cart;
import com.example.server.models.CartItems;
import com.example.server.models.Products;
import com.example.server.models.Topup;
import com.example.server.models.Transaction;
import com.example.server.models.Users;
import com.example.server.repositorys.CartItemsRepository;
import com.example.server.repositorys.CartRepository;
import com.example.server.repositorys.ProductsRepository;
import com.example.server.repositorys.TopupRepository;
import com.example.server.repositorys.TransactionRepository;
import com.example.server.repositorys.UsersRepository;
import com.example.server.services.image.ImageService;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    CartItemsRepository cartItemsRepository;
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    TopupRepository topupRepository;
    @Autowired
    ImageService imageService;
    @Override
    public TransactionResponse buy() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());
        Cart cart = cartRepository.findCartByUsers(user);
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your Cart Is Empty");
        }
        Transaction transaction = new Transaction();
        Integer totalAmount = 0;
        for (CartItems cartItem : cart.getCartItems()) {
            totalAmount += cartItem.getAmount();
            transaction.getProducts().add(cartItem.getProduct());
        }
        if (user.getSaldo() < totalAmount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your Balance is not Enough, Please Top - Up Your Account Balance");
        }
        transaction.setUsers(user);
        transaction.setDate(LocalDateTime.now());
        transaction.setTotalAmount(totalAmount);
        transactionRepository.save(transaction);
        System.out.println(transaction.getProducts());

        List<Products> products = productsRepository.findAll();
        for (Products product : products) {
            for (CartItems cartItem : cart.getCartItems()) {
                if (product.equals(cartItem.getProduct())) {
                    product.setStock(product.getStock() - cartItem.getQty());
                    productsRepository.save(product);
                }
            }
        }
        cartRepository.delete(cart);
        Set<CartItems> cartItems = cart.getCartItems();
        cartItems.forEach(i -> cartItemsRepository.delete(i));

        user.setSaldo(user.getSaldo() - totalAmount);
        usersRepository.save(user);

        List<ProductResponse> productsList = transaction.getProducts().stream().map(this::toProductResponse).collect(Collectors.toList());

        TransactionResponse response = new TransactionResponse();
        response.setDate(transaction.getDate());
        response.setId(transaction.getId());
        response.setTotalAmount(totalAmount);
        response.setProducts(productsList);
        return response;
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

    @Override
    public void topUp(Integer amount) {
        if (amount < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid Amount, Failed Top - Up");
        } else if (amount <= 10_000){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nominal Top - Up is Less, Minimum Top - Up 10k IDR");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findUsersByEmail(auth.getName());
        user.setSaldo(user.getSaldo() + amount);
        usersRepository.save(user);

        Topup topup = new Topup();
        topup.setAmount(amount);
        topup.setUsers(user);
        topup.setTopupDate(LocalDateTime.now());
        topupRepository.save(topup);
    }
}