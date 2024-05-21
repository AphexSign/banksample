package ru.yarm.banksample.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yarm.banksample.Dto.BankResponse;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Services.UserSearchService;

import java.util.List;

@RestController
@RequestMapping("/search")
public class UserSearchController {

    private final UserSearchService userSearchService;

    public UserSearchController(UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    @GetMapping
    private BankResponse<List<User>> getUsers() {
        List<User> allUsers = userSearchService.findAllUsers();
        return new BankResponse<>(allUsers.size(), allUsers);
    }

    @GetMapping("/{field}")
    private BankResponse<List<User>> getUsersWithSort(@PathVariable String field) {
        List<User> allUsers = userSearchService.findUsersWithSorting(field);
        return new BankResponse<>(allUsers.size(), allUsers);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    private BankResponse<Page<User>> getUsersWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<User> usersWithPagination = userSearchService.findUsersWithPagination(offset, pageSize);
        return new BankResponse<>(usersWithPagination.getSize(), usersWithPagination);
    }

    @GetMapping("/paginationAndSort/{offset}/{pageSize}/{field}")
    private BankResponse<Page<User>> getProductsWithPaginationAndSort(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field) {
        Page<User> usersWithPagination = userSearchService.findUsersWithPaginationAndSorting(offset, pageSize, field);
        return new BankResponse<>(usersWithPagination.getSize(), usersWithPagination);
    }


}
