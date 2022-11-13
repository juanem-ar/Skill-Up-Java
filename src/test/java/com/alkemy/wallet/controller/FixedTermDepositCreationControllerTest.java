package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.Role;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IFixedDepositService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static com.alkemy.wallet.model.ERoles.ADMIN;
import static com.alkemy.wallet.model.ERoles.USER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FixedTermDepositCreationControllerTest {

    String uri = "/fixedDeposit";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private IFixedDepositService iFdService;

    @MockBean
    private IUserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    private User user = new User();
    private User admin = new User();
    private FixedDepositDto dto = new FixedDepositDto();
    private ResponseFixedDepositDto responseDto = new ResponseFixedDepositDto();

    @BeforeEach
    void setUp() throws Exception {
        Role userRole = new Role(1L, USER, "A user", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()));
        Role adminRole = new Role(2L, ADMIN, "A role", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()));

        user.setFirstName("user");
        user.setLastName("cito");
        user.setEmail("u@gmail.com");
        user.setPassword("87654321");
        user.setCreationDate(new Timestamp(new Date().getTime()));
        user.setUpdateDate(new Timestamp(new Date().getTime()));

        admin.setFirstName("adminis");
        admin.setLastName("trador");
        admin.setEmail("a@gmail.com");
        admin.setPassword("00000000");
        admin.setCreationDate(new Timestamp(new Date().getTime()));
        admin.setUpdateDate(new Timestamp(new Date().getTime()));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findByEmail(admin.getEmail())).thenReturn(admin);

        dto.setAmount(5000.00);
        dto.setCurrency(ECurrency.ARS);
        dto.setPeriod(30);
        dto.setCreationDate(new Timestamp(new Date().getTime()));

        long daysBetween = (long)dto.getPeriod();
        dto.setClosingDate(Timestamp.valueOf(dto.getCreationDate().toLocalDateTime().plusDays(daysBetween)));

        responseDto.setAmount(dto.getAmount());
        responseDto.setInterest(dto.getAmount() * 0.05 * dto.getPeriod());
        responseDto.setCreationDate(new Timestamp(new Date().getTime()));
        responseDto.setClosingDate(dto.getClosingDate());

        when(iFdService.addFixedDeposit(user.getEmail(), dto)).thenReturn(String.valueOf(responseDto));
    }

    @Test
    public void createFixedDeposit_RightUserToken_ResponseOk() throws Exception{
        String token = jwtUtils.generateToken(
                new org.springframework.security.core.userdetails.User("u@gmail.com", "12345678", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void createFixedDeposit_RightAdminToken_ResponseOk() throws Exception{
        String token = jwtUtils.generateToken(
                new org.springframework.security.core.userdetails.User("a@gmail.com", "00000000", new ArrayList<>()));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void createFixedDeposit_TokenNotProvided_ResponseUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
