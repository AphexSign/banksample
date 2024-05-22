package ru.yarm.banksample.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "fio")
    private String fio;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @PositiveOrZero
    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "first_deposit")
    private BigDecimal first_deposit;

    @NotNull
    @Column(name = "birth")
    private LocalDate birth;
}
