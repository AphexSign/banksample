package ru.yarm.banksample.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.yarm.banksample.Models.User;

import java.time.LocalDate;

public interface UserSearchRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findByFioContaining(String name, Pageable pageable);

    Page<User> findByBirthAfter(LocalDate date, Pageable pageable);

}
