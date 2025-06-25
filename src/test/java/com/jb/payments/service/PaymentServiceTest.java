package com.jb.payments.service;

import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.mapper.PaymentMapper;
import com.jb.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    private Payment paymentType1;
/*    @Autowired
    PaymentRepository paymentRepository;*/

    @BeforeEach
    void setUp() {
        paymentType1 = new Payment();
        paymentType1.setPaymentId(1L);
        paymentType1.setAmount(999.00f);
        paymentType1.setCurrency(Currency.EUR);
        paymentType1.setDebtorIban("LT11223344556677889900");
        paymentType1.setCreditorIban("LT777888999444555000111");
        paymentType1.setDetails("For testing purposes");
    }

    @Test
    void testCreatePayment_validPayment_returnsDto() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentType1);
        PaymentPublicInputDTO savedDto = paymentService.createPayment(
                PaymentMapper.toDTO(paymentType1));
        assertEquals(1L, savedDto.getPaymentId());
        assertEquals(999.00f, savedDto.getAmount());
        assertEquals(Currency.EUR, savedDto.getCurrency());
        assertEquals("LT11223344556677889900", savedDto.getDebtorIban());
        assertEquals("LT777888999444555000111", savedDto.getCreditorIban());
        assertEquals("For testing purposes", savedDto.getDetails());
        verify(paymentRepository, times(1)).save(paymentType1);
    }

    @Test
    public void testGetPaymentForTestingType1_validId_returnSavedPaymentPublicInputDTO() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentType1));
        PaymentPublicInputDTO dto = paymentService.getPaymentByIdForTesting((1L));
        assertNotNull(dto.getPaymentId());
        assertEquals(paymentType1.getPaymentType(), dto.getPaymentType());
        assertEquals(paymentType1.getAmount(), dto.getAmount());
        assertEquals(paymentType1.getCurrency(), dto.getCurrency());
        assertEquals(paymentType1.getDebtorIban(), dto.getDebtorIban());
        assertEquals(paymentType1.getCreditorIban(), dto.getCreditorIban());
        assertEquals(paymentType1.getDetails(), dto.getDetails());
    }

    @Test
    public void testCreatePaymentType1_wrongPaymentType_throwWrongPaymentException() {
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
    public void testGetPayment_validPayment_returnsPaymentListDTO() {

        List<Payment> payments = Arrays.asList(paymentType1);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<PaymentPublicInputDTO> dtos = paymentService.getPaymentList();

        assertEquals(1, dtos.size());
        assertEquals(paymentType1.getPaymentId(), dtos.get(0).getPaymentId());
        assertEquals(paymentType1.getPaymentType(), dtos.get(0).getPaymentType());
        assertEquals(paymentType1.getAmount(), dtos.get(0).getAmount());
        assertEquals(paymentType1.getCurrency(), dtos.get(0).getCurrency());
        assertEquals(paymentType1.getDebtorIban(), dtos.get(0).getDebtorIban());
        assertEquals(paymentType1.getCreditorIban(), dtos.get(0).getCreditorIban());
        assertEquals(paymentType1.getDetails(), dtos.get(0).getDetails());

        verify(paymentRepository, times(1)).findAll();
    }

  /*  @Test
    public void testUpdatePayment_validPayment_returnsUpdatedDto() {
        Payment updated = new Payment();
        updated.setPaymentType(PaymentType.TYPE2);
        updated.setAmount(40.00f);
        updated.setCurrency(Currency.USD);
        updated.setDebtorIban("LT111111111111111");
        updated.setCreditorIban("LT2222222222222222222222");
        updated.setDetails("For pizza and drinks!");

        Payment saved = new Payment();
        saved.setPaymentId(1L);
        saved.setAmount(20.00f);
        saved.setCurrency(Currency.EUR);
        saved.setDebtorIban("LT11223344556677889900");
        saved.setCreditorIban("LT777888999444555000111");
        saved.setDetails("For pizza");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentType1));
        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);

        PaymentPublicInputDTO dto = paymentService.updatePayment(1L,
                PaymentMapper.toDTO(updated));

        assertEquals(40.0, dto.getAmount());
        assertEquals(PaymentType.TYPE2 , dto.getPaymentType());
        assertEquals(Currency.USD, dto.getCurrency());
        assertEquals("LT11223344556677889900", dto.getDebtorIban());
        assertEquals("LT777888999444555000111", dto.getCreditorIban());
        assertEquals("For pizza and drinks!", dto.getDetails());

        verify(paymentRepository).save(any(Payment.class));
    }*/
}