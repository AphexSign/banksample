package ru.yarm.banksample.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import ru.yarm.banksample.Models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByTelephone(String telephone);

    Optional<User> findByEmail(String email);

    Optional<User> findTopByTelephone(String telephone);

    Optional<User> findTopByEmail(String email);


}
