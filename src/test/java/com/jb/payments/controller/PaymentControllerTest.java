package com.jb.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jb.payments.dto.PaymentCancelDTO;
import com.jb.payments.dto.PaymentIdCancellationFeeDTO;
import com.jb.payments.dto.PaymentIdDTO;
import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.service.PaymentService;
import org.hamcrest.CoreMatchers;
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

import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    private PaymentPublicInputDTO paymentDtoType1Correct;

    private PaymentPublicInputDTO paymentDtoType2Correct;

    private PaymentIdCancellationFeeDTO paymentIdCancellationFeeDTO;

    private PaymentCancelDTO paymentCancelDTO;

    PaymentIdDTO paymentIdType1;
    PaymentIdDTO paymentIdType2;


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
                .cancelled(true)
                .build();

        paymentDtoType1Correct = PaymentPublicInputDTO.builder()
                .paymentId(1L)
                .paymentType(PaymentType.TYPE1)
                .amount(111.00f)
                .currency(Currency.EUR)
                .debtorIban("LT11223344556677889900")
                .creditorIban("LT777888999444555000111")
                .details("Details TYPE 1 for testing purposes")
                .cancelled(true)
                .cancellationFee(0f)
                .build();

        paymentIdCancellationFeeDTO = PaymentIdCancellationFeeDTO.builder()
                .paymentId(1L)
                .cancellationFee(10f)
                .build();

        paymentCancelDTO = PaymentCancelDTO.builder()
                .cancellationFee(10f)
                .build();

        paymentDtoType2Correct = PaymentPublicInputDTO.builder()
                .paymentType(PaymentType.TYPE2)
                .paymentId(1L)
                .amount(111.00f)
                .currency(Currency.EUR)
                .debtorIban("LT11223344556677889900")
                .creditorIban("LT777888999444555000111")
                .details("Details TYPE 1 for testing purposes")
                .cancelled(true)
                .build();
    }

    @Test
    public void paymentController_createValidType1Payment_returnCreated() throws Exception {
        given(paymentService.createPayment(ArgumentMatchers.any()))
                .willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentDtoType1Correct)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.paymentType",
                        CoreMatchers.is(( paymentDtoType1Correct.getPaymentType().name()))))
                .andExpect(jsonPath("$.amount",
                        CoreMatchers.is(((double) paymentDtoType1Correct.getAmount()))))
                .andExpect(jsonPath("$.currency",
                        CoreMatchers.is(paymentDtoType1Correct.getCurrency().name())))
                .andExpect(jsonPath("$.debtorIban",
                        CoreMatchers.is(paymentDtoType1Correct.getDebtorIban())))
                .andExpect(jsonPath("$.creditorIban",
                        CoreMatchers.is(paymentDtoType1Correct.getCreditorIban())))
                .andExpect(jsonPath("$.details",
                        CoreMatchers.is(paymentDtoType1Correct.getDetails())));;
    }

    @Test
    public void paymentController_getPaymentCancelledById_returnPaymentDtos() throws Exception{
        when(paymentService.getPaymentById(1L)).thenReturn(paymentIdCancellationFeeDTO);

        ResultActions response = mockMvc.perform(get("/payment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentIdCancellationFeeDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cancellationFee",
                        CoreMatchers.is(((double) paymentIdCancellationFeeDTO.getCancellationFee()))));
    }

    @Test
    public void paymentController_updatePaymentByIdToCancelled_returnCancelledTrue() throws Exception{
        when(paymentService.updatePaymentToCancelled(1L)).thenReturn(paymentCancelDTO);

        ResultActions response = mockMvc.perform(put("/payment/cancel/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentCancelDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cancelled",
                        CoreMatchers.is(( paymentCancelDTO.isCancelled()))));
    }

    @Test
    public void paymentController_getActivePayments_returnPaymentIdDtos() throws Exception{
        paymentIdType1 = PaymentIdDTO.builder().paymentId(1L).build();
        paymentIdType2 = PaymentIdDTO.builder().paymentId(2L).build();

        System.out.println(paymentIdType1.getPaymentId());
        List<PaymentIdDTO> listOfId = List.of(paymentIdType1, paymentIdType2);

        when(paymentService.getActivePayments()).thenReturn(listOfId);

        ResultActions response = mockMvc.perform(get("/active")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentIdCancellationFeeDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(listOfId.size())))
                .andExpect(jsonPath("$[0].paymentId",
                        CoreMatchers.is( paymentIdType1.getPaymentId().intValue())))
                .andExpect(jsonPath("$[1].paymentId",
                        CoreMatchers.is( paymentIdType2.getPaymentId().intValue())));
    }

}