package com.jb.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Payment paymentType1Correct;

    PaymentPublicInputDTO paymentDtoType1Correct;

    @BeforeEach
    void setUp() {
        paymentType1Correct = Payment.builder()
                .paymentType(PaymentType.TYPE1)
                .paymentId(1L)
                .amount(111.00f)
                .currency(Currency.EUR)
                .debtorIban("LT11223344556677889900")
                .creditorIban("LT777888999444555000111")
                .details("Details TYPE 1 for testing purposes")
                .build();

        paymentDtoType1Correct = PaymentPublicInputDTO.builder()
                .paymentId(1L)
                .paymentType(PaymentType.TYPE1)
                .amount(111.00f)
                .currency(Currency.EUR)
                .debtorIban("LT11223344556677889900")
                .creditorIban("LT777888999444555000111")
                .details("Details TYPE 1 for testing purposes")
                .build();
    }

    @Test
    public void paymentController_createValidPayment_returnPaymentDto() throws Exception {
        given(paymentService.createPayment(ArgumentMatchers.any()))
                .willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentDtoType1Correct)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}