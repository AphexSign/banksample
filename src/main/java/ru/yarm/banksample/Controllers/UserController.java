package ru.yarm.banksample.Controllers;

import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yarm.banksample.Dto.CredentialDto;
import ru.yarm.banksample.Dto.UserDto;
import ru.yarm.banksample.Exceptions.UserAlreadyExistsException;
import ru.yarm.banksample.Exceptions.ValueIsNegativeException;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Dto.TransferDto;
import ru.yarm.banksample.Services.JwtService;
import ru.yarm.banksample.Services.UserService;
import ru.yarm.banksample.Services.UserValidator;

import java.math.BigDecimal;

@RestController

public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserValidator userValidator;
    private final JwtService jwtService;

    public UserController(UserService userService, UserValidator userValidator, JwtService jwtService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.jwtService = jwtService;
    }


    @PostMapping("/signup")
    @Operation(summary = "Register new User", description = "Enter your user-info")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto, BindingResult bindingResult) {

        User user = User
                .builder()
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .fio(userDto.getFio())
                .telephone(userDto.getTelephone())
                .email(userDto.getEmail())
                .balance(userDto.getBalance())
                .first_deposit(userDto.getBalance())
                .birth(userDto.getBirth())
                .build();
        logger.info("Попытка регистрации нового аккаунта: "+user.getLogin());
        try {
            userValidator.validate(user, bindingResult);
            if (bindingResult.hasErrors()) throw new UserAlreadyExistsException(bindingResult.toString());
            String jwt = jwtService.generateToken(user);
            userService.saveUser(user);
            logger.info("User: "+user.getLogin()+" успешно создан");

            return ResponseEntity.ok("JWT: " + jwt);

        } catch (Exception e) {
            logger.info("Неудачная попытка регистрации нового аккаунта");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "Login to service", description = "Enter your login/password")
    public ResponseEntity<String> performLogin(@RequestBody CredentialDto credentialDto) {
        String jwt = "";
        User user = User
                .builder()
                .login(credentialDto.getLogin())
                .password(credentialDto.getPassword())
                .build();
        logger.info("Попытка входа в систему под логином: "+credentialDto.getLogin());
        try {
            if (userService.isValidCredential(user)) {
                jwt = jwtService.generateToken(user);
            }
            logger.info("Успешная попытка входа");
            return ResponseEntity.ok("Bearer " + jwt);
        } catch (Exception e) {
            logger.info("Неуспешная попытка входа");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




    @PutMapping("/change/phone")
    @Operation(summary = "Change telephone", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> updateTelephone(@RequestHeader(value = "Authorization", required = false) String token,
                                                  @RequestParam String telephone) {
        logger.info("Попытка изменения telephone");
        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                User user = userService.loadUserByLogin(jwtService.extractLogin(token));
                userService.updateTelephone(user, telephone);
            } else throw new JwtException("Wrong JWT-token credentials");
        } catch (Exception e) {
            logger.info("Неудачная попытка изменения telephone");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        logger.info("Успешная попытка изменения telephone");
        return ResponseEntity.ok("Telephone updated successfully");
    }


    @PutMapping("/change/email")
    @Operation(summary = "Change email", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> updateEmail(@RequestHeader(value = "Authorization", required = false) String token,
                                                  @RequestParam String email) {
        logger.info("Попытка изменения email");
        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                User user = userService.loadUserByLogin(jwtService.extractLogin(token));
                userService.updateEmail(user, email);
            } else throw new JwtException("Wrong JWT-token credentials");
        } catch (Exception e) {
            logger.info("Неудачная попытка изменения email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        logger.info("Успешная попытка изменения email");
        return ResponseEntity.ok("Email updated successfully");
    }





    @DeleteMapping("/email")
    @Operation(summary = "Delete email", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> deleteEmail(@RequestHeader(value = "Authorization", required = false) String token) {


        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                User user = userService.loadUserByLogin(jwtService.extractLogin(token));
                logger.info("Попытка удаления email у user: "+user.getLogin());
                userService.deleteEmail(user);
            } else throw new JwtException("Wrong JWT-token credentials");

        } catch (Exception e) {
            logger.info("Неуспешная попытка удаления email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        logger.info("Успешная попытка удаления email");
        return ResponseEntity.ok("Email deleted successfully");
    }


    @DeleteMapping("/phone")
    @Operation(summary = "Delete telephone", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> deleteTelephone(@RequestHeader(value = "Authorization", required = false) String token) {

        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                User user = userService.loadUserByLogin(jwtService.extractLogin(token));
                logger.info("Попытка удаления telephone у user: "+user.getLogin());
                userService.deleteTelephone(user);
            } else throw new JwtException("Wrong JWT-token credentials");

        } catch (Exception e) {
            logger.info("Неуспешная попытка удаления telephone");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        logger.info("Успешная попытка удаления telephone");
        return ResponseEntity.ok("Telephone deleted successfully");
    }


    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> tryTotransfer(@RequestHeader(value = "Authorization", required = false) String token,
                                                @RequestBody TransferDto transferDto) {
        logger.info("Попытка перевода денежных средств User: "+transferDto.getUsername());
        try {
            if (StringUtils.isBlank(token)) throw new JwtException("Token is empty");
            token = token.substring(7);
            if (jwtService.isTokenValid(token)) {
                String recepient = transferDto.getUsername();
                BigDecimal amount = new BigDecimal(transferDto.getAmount());
                if (amount.compareTo(BigDecimal.ZERO) < 0) throw new ValueIsNegativeException("Недопустимое значение");
                User payer = userService.loadUserByLogin(jwtService.extractLogin(token));
                userService.tryTransfer(payer, recepient, amount);
                logger.info("Сумма: "+amount+" от User:"+payer.getLogin()+" к User:"+recepient+" переведена");
            } else throw new JwtException("Wrong JWT-token credentials");

        } catch (Exception e) {
            logger.info("Неудачная попытка перевода средств");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        logger.info("Транзакция завершена.");
        return ResponseEntity.ok("All right");
    }

}
