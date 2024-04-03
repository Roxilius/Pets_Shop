package com.example.server.models;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carts {
    @Id
    @UuidGenerator
    @Column(name = "id",length = 36,nullable = false)
    private String id;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @ManyToMany
    @JoinTable(name = "cart_products",joinColumns = @JoinColumn(name = "cart_id"),inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Products> products = new HashSet<>();
    
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id", nullable = false)
    private Users users;
}
