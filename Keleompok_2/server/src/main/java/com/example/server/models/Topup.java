package com.example.server.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topup {
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, nullable=false)
    private String id;

    @Column(name = "topup_date")
    private LocalDateTime topupDate;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id", nullable = false)
    private Users users;
}
