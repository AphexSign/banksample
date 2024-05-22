package ru.yarm.banksample.Services;

import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidator implements Validator {

    private final UserService userService;

    private static final String EMAIL_PATTERN = "^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private boolean isEmailPatternValid(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public UserValidator( UserService userService) {

        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (!StringUtils.isBlank(user.getEmail()) || !StringUtils.isBlank(user.getTelephone())) {
            validate_login(user, errors);
            validate_password(user, errors);
            validate_fio(user, errors);
            validate_email(user, errors);
            validate_telephone(user, errors);
        } else errors.rejectValue("email", "", "Telephone or email must be initialized!");
    }

    public void validate_login(Object target, Errors errors) {
        // Вначале стоит проверить на то, наличие хотя бы одного условия email!=null или telep!=null,
        User user = (User) target;

        if (StringUtils.isBlank(user.getLogin())) {
            errors.rejectValue("login", "length", "login can't be blank");
        }

        try {
            userService.loadUserByLogin(user.getLogin());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("login", "", "Login already in use");
    }


    public void validate_password(Object target, Errors errors) {
        User user = (User) target;
        if (StringUtils.isBlank(user.getPassword())) {
            errors.rejectValue("password", "length", "Password can't be blank");
        }
        if (user.getPassword().length() < 3) {
            errors.rejectValue("password", "length", "Password can't be less 3 characters");
        }

    }

    public void validate_fio(Object target, Errors errors) {
        User user = (User) target;

        if (StringUtils.isBlank(user.getFio())) {
            errors.rejectValue("fio", "length", "Fio can't be blank");
        }
        if (user.getFio().length() < 3) {
            errors.rejectValue("fio", "length", "Fio can't be less 3 characters");
        }

    }


    public void validate_email(Object target, Errors errors) {
        User user = (User) target;
        // Если человека было не найдено будем смотреть, если он не пустой - то соответствует ли паттерну?
        if (!StringUtils.isBlank(user.getEmail())) {
            if (!isEmailPatternValid(user.getEmail())) throw new RuntimeException("Invalid email format");
        }
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
