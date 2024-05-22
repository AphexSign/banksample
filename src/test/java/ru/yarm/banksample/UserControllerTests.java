package ru.yarm.banksample;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yarm.banksample.Dto.CredentialDto;
import ru.yarm.banksample.Dto.TransferDto;
import ru.yarm.banksample.Services.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    protected MockMvc mockMvc;

    @Spy
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    // Правильные входные данные у User
    @Test
    public void tryToEnterMethodSuccess() throws Exception {
        CredentialDto credentialDto = new CredentialDto("user9", "pass5");
        String payer = jsonToString(credentialDto);

         mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payer)).andExpect(status().isOk()).andReturn();
    }

    // Неправильные входные данные у User
    @Test
    public void tryToEnterFail() throws Exception {
        CredentialDto credentialDto = new CredentialDto("user9", "p5");
        String payer = jsonToString(credentialDto);

         mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payer)).andExpect(status().is(500)).andReturn();
    }

    // Перевод превышающий наш лимит суммы средств
    @Test
    public void tryToTransferNotEnough() throws Exception {
        // Переводим сумму, которой не хватает
        TransferDto transferDto = new TransferDto("user11", "10000");
        CredentialDto credentialDto = new CredentialDto("user9", "pass5");
        String payer = jsonToString(credentialDto);
        String receiver = jsonToString(transferDto);

        MvcResult jwtResult = mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payer)).andExpect(status().isOk()).andReturn();

        String payerToken = jwtResult.getResponse().getContentAsString();

         mockMvc.perform(post("/transfer")
                .header("Authorization", payerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(receiver)).andDo(print()).andExpect(status().is(500)).andReturn();
    }

    // Перевод допустимой суммы средств
    @Test
    public void tryToTransferEnough() throws Exception {
        TransferDto transferDto = new TransferDto("user11", "10");
        CredentialDto credentialDto = new CredentialDto("user9", "pass5");
        String payer = jsonToString(credentialDto);
        String receiver = jsonToString(transferDto);

        MvcResult jwtResult = mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payer)).andExpect(status().isOk()).andReturn();

        String payerToken = jwtResult.getResponse().getContentAsString();

         mockMvc.perform(post("/transfer")
                .header("Authorization", payerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(receiver)).andDo(print()).andExpect(status().is(200)).andReturn();
    }

    // Перевод отрицательной суммы
    @Test
    public void tryToTransferNegative() throws Exception {
        TransferDto transferDto = new TransferDto("user11", "-10");
        CredentialDto credentialDto = new CredentialDto("user9", "pass5");
        String payer = jsonToString(credentialDto);
        String receiver = jsonToString(transferDto);

        MvcResult jwtResult = mockMvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payer)).andExpect(status().isOk()).andReturn();
        String payerToken = jwtResult.getResponse().getContentAsString();

        mockMvc.perform(post("/transfer")
                .header("Authorization", payerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(receiver)).andDo(print()).andExpect(status().is(500)).andReturn();
    }
    private String jsonToString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }


}
