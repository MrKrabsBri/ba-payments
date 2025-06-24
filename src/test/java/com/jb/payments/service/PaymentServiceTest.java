package com.jb.payments.service;

import com.jb.payments.dto.PaymentCancelDTO;
import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.mapper.PaymentMapper;
import com.jb.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

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
        PaymentPublicInputDTO actual = paymentService.getPaymentByIdForTesting(1L);

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
                .paymentId(1L)
                .paymentType(PaymentType.TYPE1)
                .amount(100.00f)
                .currency(Currency.EUR)
                .debtorIban("LT123456789")
                .creditorIban("LT9999456789")
                .details("For drinks")
                .canceled(false)
                .cancellationFee(null) // .
                .dateOfCreation(LocalDate.of(2025, 6, 23))
                .timeOfCreation(LocalTime.of(10, 1, 10))
                .build();

        System.out.println("expected" + expected);

        Payment p = PaymentMapper.toEntity(expected);

        Payment pp = paymentRepository.save(p);
        System.out.println("id created  : "+ pp.getPaymentId());
        //PaymentPublicInputDTO output = paymentService.createPayment(expected);
        //PaymentPublicInputDTO dto = paymentService.getPaymentById(1L);
        //long recordId = paymentService.getPaymentById(createdId).getPaymentId();
        PaymentCancelDTO updated = paymentService.updatePaymentToCancelled(pp.getPaymentId());
        System.out.println("updated " + updated);

        PaymentPublicInputDTO actual = paymentService.getPaymentByIdForTesting(pp.getPaymentId());
        System.out.println("retrieved after cancel: " + actual);

        assertTrue(updated.isCancelled());
        assertTrue(updated.getCancellationFee() > 0);

    }

}