package ru.yarm.banksample.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDto {
    private String username;
    private String amount;
}
