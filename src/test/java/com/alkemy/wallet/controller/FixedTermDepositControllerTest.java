package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedDepositDto;
import com.alkemy.wallet.dto.ResponseSimulatedFixedDepositDto;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.security.service.JwtUtils;
import com.alkemy.wallet.service.IFixedDepositService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { FixedTermDepositController.class })
@ContextConfiguration(classes = { FixedTermDepositController.class })
public class FixedTermDepositControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFixedDepositService fixedDepositService;

    @MockBean
    private JwtUtils jwtUtils;

    ObjectMapper objectMapper;

    @BeforeEach
    void config(){
        objectMapper = new ObjectMapper();
    }

    private String uri = "/fixedDeposit/simulate";

    @Test
    void getSimulateFixedDeposit_RequestWithBody_ResponseSimulatedFixedDepositDto()
    throws Exception{

        FixedDepositDto body = new FixedDepositDto();
        body.setAmount(3000.00);
        body.setCurrency(ECurrency.ARS);
        body.setPeriod(30);

        ResponseSimulatedFixedDepositDto responseBody = new ResponseSimulatedFixedDepositDto();
        responseBody.setAmount(3000.00);
        responseBody.setCurrency(ECurrency.ARS);
        responseBody.setPeriod(30);

        when(fixedDepositService.simulateFixedDeposit(body)).thenReturn(responseBody);

        mockMvc.
                perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(3000.00))
                .andExpect(jsonPath("$.currency").value("ARS"))
                .andExpect(jsonPath("$.period").value(30));
    }

}
