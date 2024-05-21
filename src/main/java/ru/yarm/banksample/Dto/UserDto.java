package ru.yarm.banksample.Dto;


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
public class UserDto {

    private String login;
    private String password;
    private String fio;
    private String telephone;
    private String email;
    private BigDecimal balance;
    private LocalDate date_birth;

}
