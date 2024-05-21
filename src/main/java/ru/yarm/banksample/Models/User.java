package ru.yarm.banksample.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotEmpty(message = "Login can't me empty")
    @Size(min = 3, max = 100, message = "Login can't be less 3 symbols")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Password can't me empty")
    @Size(min = 3, max = 100, message = "Password can't be less 3 symbols")
    @Column(name = "password")
    private String password;

    @Column(name = "fio")
    private String fio;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "first_deposit")
    private BigDecimal first_deposit;

    @Column(name="date_birth")
    private LocalDate date_birth;
}
