package com.ouz.atmsimulation.unittest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ouz.atmsimulation.controller.ATMController;
import com.ouz.atmsimulation.controller.dto.ATMOperationDTO;
import com.ouz.atmsimulation.enums.TransactionType;
import com.ouz.atmsimulation.service.ATMServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ATMController.class)
@ContextConfiguration(classes = ATMController.class)
public class TestATMController {

    @MockBean
    private ATMServiceImpl atmService;

    protected MockMvc mockMvc;

    private ATMOperationDTO dto;

    private String requestJson;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) throws JsonProcessingException
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        dto = new ATMOperationDTO();
        dto.setAccountNumber(123456789L);
        dto.setPin(1234L);
        dto.setAmount(new BigDecimal("800"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson = ow.writeValueAsString(dto );
    }

    @Test
    public void testAccountInfo() throws Exception
    {
        this.mockMvc.perform(get("/atm/expose",dto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(atmService).exposeAccount(dto.getAccountNumber(),dto.getPin());
    }

    @Test
    public void testWithdraw() throws Exception
    {

        this.mockMvc.perform(put("/atm/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(atmService).atmOperation(dto.getAccountNumber(),dto.getPin(),dto.getAmount(), TransactionType.WITHDRAW);
    }

    @Test
    public void testDeposit() throws Exception
    {

        this.mockMvc.perform(put("/atm/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(atmService).atmOperation(dto.getAccountNumber(),dto.getPin(),dto.getAmount(), TransactionType.DEPOSIT);
    }

}
