package com.jb.payments.service;

import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.WrongPaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

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

    public void createPaymentType1_wrongPaymentType_throwWrongPaymentException() {
        PaymentPublicInputDTO input = PaymentPublicInputDTO.builder()
                .paymentType(PaymentType.TYPE1)
                .amount(100.00f)
                .currency(Currency.USD)
                .debtorIban("LT123456789")
                .creditorIban("LT9999456789")
                .details("Rent")
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
                .details("Uz pirkinius")
                .build();

        PaymentPublicInputDTO output = paymentService.createPayment(expected);
        PaymentPublicInputDTO actual = paymentService.getPaymentById(1L);

        assertNotNull(actual.getPaymentId());
        assertEquals(expected.getPaymentType(), actual.getPaymentType());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getCurrency(), actual.getCurrency());
        assertEquals(expected.getDebtorIban(), actual.getDebtorIban());
        assertEquals(expected.getCreditorIban(), actual.getCreditorIban());
        assertEquals(expected.getDetails(), actual.getDetails());
    }

    @Test
    public void updatePaymentToCanceled_validPaymentType1_canceledFieldTrue(){
        PaymentPublicInputDTO expected = PaymentPublicInputDTO.builder()
                .paymentType(PaymentType.TYPE1)
                .amount(100.00f)
                .currency(Currency.EUR)
                .debtorIban("LT123456789")
                .creditorIban("LT9999456789")
                .details("For drinks")
                .canceled(false)
                .cancellationFee(0.05f)
                .build();

        Long createdId =  paymentService.createPayment(expected).getPaymentId();
        //PaymentPublicInputDTO output = paymentService.createPayment(expected);
        //PaymentPublicInputDTO dto = paymentService.getPaymentById(1L);
        //long recordId = paymentService.getPaymentById(createdId).getPaymentId();
        PaymentPublicInputDTO updated = paymentService.updatePaymentToCancelled(createdId);

        assertTrue(updated.isCanceled());
        assertTrue(updated.getCancellationFee() > 0);

    }

}