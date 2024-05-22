package ru.yarm.banksample.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Repositories.UserRepository;
import ru.yarm.banksample.Repositories.UserSearchRepository;

import java.time.LocalDate;

@Service
public class UserSearchService {

    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;

    public UserSearchService(UserRepository userRepository, UserSearchRepository userSearchRepository) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
    }


    public Page<User> findUserByFioLikePagSort(String fio, int offset, int pageSize, String field) {
        return userSearchRepository.findByFioContaining(fio, PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
    }

    public Page<User> findUserByDatePagSort(LocalDate date, int offset, int pageSize, String field) {
        return userSearchRepository.findByBirthAfter(date, PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
    }

    public User findUserByTelephone(String telephone) {
        return userRepository.findByTelephone(telephone).orElseThrow(()->new UsernameNotFoundException("User is not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User is not found"));
    }



}
