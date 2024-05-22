package ru.yarm.banksample.Controllers;


import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Services.JwtService;
import ru.yarm.banksample.Services.UserSearchService;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/search")
public class UserSearchController {

    private final UserSearchService userSearchService;
    private final JwtService jwtService;

    public UserSearchController(UserSearchService userSearchService, JwtService jwtService) {
        this.userSearchService = userSearchService;
        this.jwtService = jwtService;
    }

    @GetMapping("/byFio")
    @Operation(summary = "search by FIO", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> searchFio(@RequestHeader(value = "Authorization", required = false) String token,
                                            @RequestParam String fio,
                                            @RequestParam(required = false, defaultValue = "id") String field,
                                            @RequestParam(required = false, defaultValue = "0") int offset,
                                            @RequestParam(required = false, defaultValue = "5") int pagesize) {

        List<String> users;
        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                Page<User> result = userSearchService.findUserByFioLikePagSort(fio, offset, pagesize, field);
                users = result.getContent().stream().map(User::toString).toList();
            } else throw new JwtException("Wrong JWT-token credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(users.toString());
    }


    @GetMapping("/byDate")
    @Operation(summary = "search by Date", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> searchDate(@RequestHeader(value = "Authorization", required = false) String token,
                                             @RequestParam LocalDate date,
                                             @RequestParam(required = false, defaultValue = "id") String field,
                                             @RequestParam(required = false, defaultValue = "0") int offset,
                                             @RequestParam(required = false, defaultValue = "5") int pagesize) {

        List<String> users;
        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                Page<User> result = userSearchService.findUserByDatePagSort(date, offset, pagesize, field);
                users = result.getContent().stream().map(User::toString).toList();
            } else throw new JwtException("Wrong JWT-token credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(users.toString());
    }


    @GetMapping("/byPhone")
    @Operation(summary = "search by telephone", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> searchPhone(@RequestHeader(value = "Authorization", required = false) String token,
                                              @RequestParam String telephone) {
        User user;
        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {

                user = userSearchService.findUserByTelephone(telephone);
            } else throw new JwtException("Wrong JWT-token credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(user.toString());
    }


    @GetMapping("/byEmail")
    @Operation(summary = "search by email", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> searchEmail(@RequestHeader(value = "Authorization", required = false) String token,
                                              @RequestParam String email) {
        User user;
        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                user = userSearchService.findUserByEmail(email);
            } else throw new JwtException("Wrong JWT-token credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(user.toString());
    }


}
