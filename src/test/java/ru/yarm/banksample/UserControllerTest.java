package ru.yarm.banksample;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.yarm.banksample.Controllers.UserController;
import ru.yarm.banksample.Dto.CredentialDto;
import ru.yarm.banksample.Models.User;
import ru.yarm.banksample.Repositories.UserRepository;
import ru.yarm.banksample.Services.JwtService;
import ru.yarm.banksample.Services.UserService;




//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(value = UserController.class, includeFilters = {
//@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtService.class)})

@WebMvcTest(value = UserController.class, includeFilters = {
        // to include JwtUtil in spring context
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtService.class)})

public class UserControllerTest {


    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private User user;

    @InjectMocks
    private UserController userController;

   @Autowired
    private MockMvc mockMvc;

    private static String jwtToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
       user = User.builder().login("user9").password("pass5").build();
        jwtToken = jwtService.generateToken(user);

    }

    @Test
    void acquireTkn() throws Exception {

        System.out.println(jwtToken);

    }


    // Нужно тестировать
    @Test
    void transferMoney() throws Exception {
        // На вход мы получаем JWT токен, который передается или создается
        // Лучше брать его из базы данных. Тоесть мы берем двух юзеров из база данных
//        CredentialDto credentialDto = new CredentialDto("user9", "pass5");
//
//        String body = asJsonString(credentialDto);
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signin")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                        .content(body))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        response = response.replace("{\"access_token\": \"", "");
//        String token = response.replace("\"}", "");
//
//        System.out.println(token);

//        String jwt="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYW5rQXBwIiwiaXNzIjoiU2lnbkhvbGRlciIsImV4cCI6MTcxNjI5MjQ1MiwibG9naW4iOiJzdHJpbmcifQ.Cvkhi11X5zUQCPvbHZWhlutHTJJ2LhUCUxS9VzsvaWk";
//
//        TransferDto transferDto = new TransferDto("user7", "50");
//
//        mockMvc.perform(post("/transfer")
//                        .header("Authorization", "Bearer "+jwt)
//                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                        .content(asJsonString(transferDto)))
//                .andDo(print())
//                .andExpect(status().isOk()).andDo(print())
//                .andReturn();
    }


    // Метод для преобразования объекта в JSON строку
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // На вход мы получаем JWT токен, который передается или создается
    // Лучше брать его из базы данных. Тоесть мы берем двух юзеров из база данных

    // Мы должны протестировать, что у нас работает JWT - проверщик
    // JWT - верный
    // JWT - неверный
    // Нужно предусмотреть, чтобы наше число было именно числом, а не буквой
    // Нужно предусмотреть, чтобы число было больше нуля!! А не отрицательное
    // Мы должны проверить, что мы переводим допустимую сумму
    // Мы должны проверить, что мы переводим сумму превышующую нашу сумму
    // Мы должны проверить, что переводит человек, которого нет в базе
    // Мы должны проверить, что человека - адреса не существует


//
//    @PostMapping("/transfer")
//    @Operation(summary = "Transfer funds", security = @SecurityRequirement(name = "bearerAuth"))
//    public ResponseEntity<String> tryTotransfer(@RequestHeader(value = "Authorization", required = false) String token,
//                                                @RequestBody TransferDto transferDto) {
//        logger.info("Попытка перевода денежных средств User: "+transferDto.getUsername());
//        try {
//            token = token.substring(7);
//            if (jwtService.isTokenValid(token)) {
//                String recepient = transferDto.getUsername();
//                BigDecimal amount = new BigDecimal(transferDto.getAmount());
//                User payer = userService.loadUserByLogin(jwtService.extractLogin(token));
//                userService.tryTransfer(payer, recepient, amount);
//                logger.info("Сумма: "+amount+" от User:"+payer.getLogin()+" к User:"+recepient+" переведена");
//            } else throw new JwtException("Wrong JWT-token credentials");
//
//        } catch (Exception e) {
//            logger.info("Неудачная попытка перевода средств");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//        logger.info("Транзакция завершена.");
//        return ResponseEntity.ok("All right");
//    }


}
