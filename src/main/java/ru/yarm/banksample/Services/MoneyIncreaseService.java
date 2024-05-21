package ru.yarm.banksample.Services;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Repositories.UserRepository;

import java.math.BigDecimal;

@Service
public class MoneyIncreaseService {

    private final UserRepository userRepository;
    public MoneyIncreaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void increaseMoney() {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            BigDecimal maxValue = user.getFirst_deposit().multiply(new BigDecimal("2.07"));
            BigDecimal adder = user.getBalance().multiply(new BigDecimal("0.05"));
            BigDecimal balance = user.getBalance();
            if (adder.compareTo(maxValue) <= 0) {
                balance = balance.add(adder);
                user.setBalance(balance);
            } else {
                balance = balance.add(maxValue);
                user.setBalance(balance);
            }
            userRepository.save(user);
        }
    }
}
