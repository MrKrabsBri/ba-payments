package com.jb.payments.service;

import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static java.util.stream.Stream.builder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void createPaymentType1_validPayment_returnSavedPaymentDTO() {
        PaymentPublicInputDTO expected = PaymentPublicInputDTO.builder()
                .paymentType(PaymentType.TYPE1)
                .amount(100.00f)
                .currency(Currency.EUR)
                .debtorIban("LT123456789")
                .creditorIban("LT9999456789")
                .details("Bowling")
                .build();

        PaymentPublicInputDTO output = paymentService.createPayment(expected);

        assertNotNull(output.getPaymentId());
        assertEquals(expected.getPaymentType(), output.getPaymentType());
        assertEquals(expected.getAmount(), output.getAmount());
        assertEquals(expected.getCurrency(), output.getCurrency());
        assertEquals(expected.getDebtorIban(), output.getDebtorIban());
        assertEquals(expected.getCreditorIban(), output.getCreditorIban());
        assertEquals(expected.getDetails(), output.getDetails());
    }

    @Test
    public void createPaymentType1_wrongPaymentType_throwException() {
        PaymentPublicInputDTO input = PaymentPublicInputDTO.builder()
                .paymentType(PaymentType.TYPE1)
                .amount(100.00f)
                .currency(Currency.USD)
                .debtorIban("LT123456789")
                .creditorIban("LT9999456789")
                .details("Bowling")
                .build();

        // Act & Assert: expect WrongPaymentException
        WrongPaymentException ex = assertThrows(WrongPaymentException.class, () -> {
            paymentService.createPayment(input);
        });

        assertEquals("Payment of TYPE1 must use currency EUR", ex.getMessage());
    }

    @Test
    public void getPayment_validPayment_returnsPaymentDTO(){
        PaymentPublicInputDTO expected = PaymentPublicInputDTO.builder()
                .paymentType(PaymentType.TYPE1)
                .amount(100.00f)
                .currency(Currency.EUR)
                .debtorIban("LT123456789")
                .creditorIban("LT9999456789")
                .details("Bowling")
                .build();

        PaymentPublicInputDTO output = paymentService.createPayment(expected);

        assertNotNull(output.getPaymentId());
        assertEquals(expected.getPaymentType(), output.getPaymentType());
        assertEquals(expected.getAmount(), output.getAmount());
        assertEquals(expected.getCurrency(), output.getCurrency());
        assertEquals(expected.getDebtorIban(), output.getDebtorIban());
        assertEquals(expected.getCreditorIban(), output.getCreditorIban());
        assertEquals(expected.getDetails(), output.getDetails());
    }


}