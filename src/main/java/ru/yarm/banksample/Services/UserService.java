package ru.yarm.banksample.Services;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.yarm.banksample.Exceptions.*;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User loadUserByLogin(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) throw new UsernameNotFoundException("User with login: '" + login + "' is not found");
        return user.get();
    }

    public User loadUserByTelephone(String telephone) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findTopByTelephone(telephone);
        if (user.isEmpty()) throw new UsernameNotFoundException("User is not found");
        if (StringUtils.isBlank(telephone))
            throw new UsernameNotFoundException("Blank is used for telephone");
        return user.get();
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findTopByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("User is not found");
        if (StringUtils.isBlank(email)) throw new UsernameNotFoundException("Blank is used for email");
        return user.get();
    }

    @Transactional
    public void tryTransfer(User payer, String recipient, BigDecimal amount) {
        User recip = loadUserByLogin(recipient);
        if (!recip.getLogin().equals(payer.getLogin())) {
            BigDecimal differ = payer.getBalance().subtract(amount);
            if (differ.compareTo(BigDecimal.ZERO) >= 0) {
                payer.setBalance(payer.getBalance().subtract(amount));
                recip.setBalance(recip.getBalance().add(amount));
                userRepository.save(payer);
                userRepository.save(recip);
            } else throw new InsufficientFundsException("User " + payer.getLogin() + " don't have enough funds");
        } else throw new SameAccountTransferException("Can't transfer funds to same account");
    }

    public boolean isValidCredential(User userChecked) {
        boolean result ;
        User userReal = loadUserByLogin(userChecked.getLogin());
        if (userReal != null) {
            if (userReal.getPassword().equals(userChecked.getPassword())) {
                result = true;
            } else throw new UserWrongCredentials("Password is incorrect");

        } else throw new UsernameNotFoundException("");
        return result;
    }

    @Transactional
    public void updateEmail(User user, String newEmail) {
        User sameUser;
        // Проверка на email-двойник
        try {
            sameUser = loadUserByEmail(newEmail);
        } catch (UsernameNotFoundException e) {
            sameUser = null;
        }
        if (sameUser != null) throw new UserDataAlreadyPresentException("Email already present in DB");
        // Если нет двойников, и такой юзер существует
        if (user != null) {
            if (!StringUtils.isBlank(newEmail)) {
                user.setEmail(newEmail);
                userRepository.save(user);
            }
            // Если новый вариант представляет собой пустоту
            if (StringUtils.isBlank(newEmail)) {
                if (isDetelableEmail(user)) {
                    user.setEmail(newEmail);
                    userRepository.save(user);
                } else throw new ZeroContactInfoException("Can't delete all contacts from user");
            }
        } else throw new UsernameNotFoundException("");
    }

    @Transactional
    public void updateTelephone(User user, String newTelephone) {
        User sameUser;
        // Проверка на телефон-двойник
        try {
            sameUser = loadUserByTelephone(newTelephone);
        } catch (UsernameNotFoundException e) {
            sameUser = null;
        }

        if (sameUser != null) throw new UserDataAlreadyPresentException("Telephone already present in DB");

        if (user != null) {
            if (!StringUtils.isBlank(newTelephone)) {
                user.setTelephone(newTelephone);
                userRepository.save(user);
            }
            if (StringUtils.isBlank(newTelephone)) {
                if (isDetelableTelephone(user)) {
                    user.setTelephone(newTelephone);
                    userRepository.save(user);
                } else throw new ZeroContactInfoException("Can't delete all contacts from user");
            }
        } else throw new UsernameNotFoundException("");
    }

    // Удаляем только так почту, чтобы оставались контакты
    @Transactional
    public void deleteEmail(User user) {
        if (user != null) {
            if (isDetelableEmail(user)) {
                if (StringUtils.isBlank(user.getEmail())) throw new AlreadyDeletedException("email is already blank!");
                user.setEmail(null);
                userRepository.save(user);
            } else throw new ZeroContactInfoException("Can't delete all contacts from user");
        } else throw new UsernameNotFoundException("");
    }

    // Удаляем только так телефон, чтобы оставались контакты
    @Transactional
    public void deleteTelephone(User user) {
        if (user != null) {
            if (isDetelableTelephone(user)) {
                if (StringUtils.isBlank(user.getTelephone()))
                    throw new AlreadyDeletedException("Phone is already blank!");
                user.setTelephone(null);
                userRepository.save(user);
            } else throw new ZeroContactInfoException("Can't delete all contacts from user");
        } else throw new UsernameNotFoundException("");
    }


    public boolean isDetelableEmail(User user) {
        return !StringUtils.isBlank(user.getTelephone());
    }

    public boolean isDetelableTelephone(User user) {
        return !StringUtils.isBlank(user.getEmail());
    }

}
