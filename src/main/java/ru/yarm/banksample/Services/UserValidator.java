package ru.yarm.banksample.Services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Repositories.UserRepository;

@Service
public class UserValidator implements Validator {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserValidator(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (!user.getEmail().equals("") || !user.getTelephone().equals("")) {
            validate_login(user, errors);
            validate_email(user, errors);
            validate_telephone(user, errors);
        } else errors.rejectValue("email", "", "Telephone or email must be initialized!");
    }

    public void validate_login(Object target, Errors errors) {
        // Вначале стоит проверить на то, наличие хотя бы одного условия email!=null или telep!=null,

        User user = (User) target;
        try {
            userService.loadUserByLogin(user.getLogin());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("login", "", "Login already in use");
    }

    public void validate_email(Object target, Errors errors) {
        User user = (User) target;
        try {
            userService.loadUserByEmail(user.getEmail());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("email", "", "Email already in use");
    }

    public void validate_telephone(Object target, Errors errors) {
        User user = (User) target;
        try {
            userService.loadUserByTelephone(user.getTelephone());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("telephone", "", "Telephone already in use");
    }


}
