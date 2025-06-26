package com.jb.payments.service;

import com.jb.payments.calculator.CancellationFeeCalculator;
import com.jb.payments.dto.PaymentIdCancellationFeeDTO;
import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.mapper.PaymentMapper;
import com.jb.payments.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
@Transactional
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    private Payment paymentType1Correct;
    private Payment paymentType2Correct;
    private Payment paymentType3Correct;

    private PaymentPublicInputDTO paymentDtoType1Incorrect;

    PaymentPublicInputDTO paymentDtoType1Correct;
    PaymentPublicInputDTO paymentDtoType2Correct;
    PaymentPublicInputDTO paymentDtoType3Correct;

    private PaymentPublicInputDTO paymentDtoType3Incorrect;

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

        paymentDtoType1Incorrect = PaymentPublicInputDTO.builder()
                .paymentType(PaymentType.TYPE1)
                .amount(100.00f)
                .currency(Currency.USD)
                .debtorIban("LT123456789")
                .creditorIban("LT9999456789")
                .details("Rent")
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

        paymentType2Correct = Payment.builder()
                .paymentType(PaymentType.TYPE2)
                .paymentId(2L)
                .amount(333.00f)
                .currency(Currency.USD)
                .debtorIban("LT11223344556677889900")
                .creditorIban("LT777888999444555000111")
                .details("Optional for testing")
                .build();

        paymentDtoType2Correct = PaymentPublicInputDTO.builder()
                .paymentId(2L)
                .paymentType(PaymentType.TYPE2)
                .amount(222.00f)
                .currency(Currency.USD)
                .debtorIban("LT11223344556677889900")
                .creditorIban("LT777888999444555000111")
                .details("Details TYPE 2 for testing purposes")
                .build();

        paymentType3Correct = Payment.builder()
                .paymentType(PaymentType.TYPE3)
                .paymentId(3L)
                .amount(555.00f)
                .currency(Currency.EUR)
                .debtorIban("LT11223344556677889900")
                .creditorIban("LT777888999444555000111")
                .details("Details TYPE 3 for testing purposes")
                .build();

        paymentDtoType3Correct = PaymentPublicInputDTO.builder()
                .paymentId(3L)
                .paymentType(PaymentType.TYPE3)
                .amount(333.00f)
                .currency(Currency.USD)
                .debtorIban("DIbanType3")
                .creditorIban("CIbanType3")
                .creditorBankBicCode("Creditor bank BIC code for  TYPE 3 for testing purposes")
                .build();

        paymentDtoType3Incorrect = PaymentPublicInputDTO.builder()
                .paymentId(3L)
                .paymentType(PaymentType.TYPE3)
                .amount(333.00f)
                .currency(Currency.USD)
                .debtorIban("DIbanType3")
                .creditorIban("CIbanType3")
                .details("Details TYPE 3 for testing purposes")
                .build();
    }

    @Test
    void testPaymentCancellationFeeCalculator_paymentType1_calculationCorrect() {
        int hoursPassed = 10;
        assertEquals(CancellationFeeCalculator.COEFFICIENT_TYPE_1 * hoursPassed,
                CancellationFeeCalculator.calculateCancellationFee(paymentType1Correct, hoursPassed));
    }

    @Test
    void testPaymentCancellationFeeCalculator_paymentType2_calculationCorrect() {
        int hoursPassed = 10;
        assertEquals(CancellationFeeCalculator.COEFFICIENT_TYPE_2 * hoursPassed,
                CancellationFeeCalculator.calculateCancellationFee(paymentType2Correct, hoursPassed));
    }

    @Test
    void testPaymentCancellationFeeCalculator_paymentType3_calculationCorrect() {
        int hoursPassed = 10;
        assertEquals(CancellationFeeCalculator.COEFFICIENT_TYPE_3 * hoursPassed,
                CancellationFeeCalculator.calculateCancellationFee(paymentType3Correct, hoursPassed));
    }

    @Test
    void testCreatePayment_paymentType1Valid_returnDto() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentType1Correct);
        PaymentPublicInputDTO savedDto = paymentService.createPayment(
                PaymentMapper.toDTO(paymentType1Correct));
        assertEquals(1L, savedDto.getPaymentId());
        assertEquals(111.00f, savedDto.getAmount());
        assertEquals(Currency.EUR, savedDto.getCurrency());
        assertEquals("LT11223344556677889900", savedDto.getDebtorIban());
        assertEquals("LT777888999444555000111", savedDto.getCreditorIban());
        assertEquals("Details TYPE 1 for testing purposes", savedDto.getDetails());
    }

    @Test
    public void testCreatePayment_validPaymentType2_returnPaymentDto() {
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(paymentType2Correct);
        PaymentPublicInputDTO savedPayment = paymentService.createPayment(paymentDtoType2Correct);
        Assertions.assertThat(savedPayment).isNotNull();
    }

    @Test
    public void testCreatePayment_incorrectPaymentType3WithDetailsField_throwWrongPaymentException() {
        Payment savedEntity = new Payment();
        savedEntity.setPaymentId(3L);
        when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(savedEntity);

        WrongPaymentException ex = assertThrows(
                WrongPaymentException.class,
                () -> paymentService.createPayment(paymentDtoType3Incorrect)
        );
        assertEquals("Payment of TYPE3 must have creditor bank BIC code specified",
                ex.getMessage());
    }

    @Test
    void testGetPaymentForTestingType1_validId_returnSavedPaymentPublicInputDTO() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentType1Correct));
        PaymentPublicInputDTO dto = paymentService.getPaymentByIdForTesting((1L));
        assertAll("Verify mapping from Entity → DTO",
                () -> assertNotNull(dto.getPaymentId(), "paymentId should not be null"),
                () -> assertEquals(paymentType1Correct.getPaymentType(), dto.getPaymentType(), "paymentType"),
                () -> assertEquals(paymentType1Correct.getAmount(), dto.getAmount(), "amount"),
                () -> assertEquals(paymentType1Correct.getCurrency(), dto.getCurrency(), "currency"),
                () -> assertEquals(paymentType1Correct.getDebtorIban(), dto.getDebtorIban(), "debtorIban"),
                () -> assertEquals(paymentType1Correct.getCreditorIban(), dto.getCreditorIban(), "creditorIban"),
                () -> assertEquals(paymentType1Correct.getDetails(), dto.getDetails(), "details")
        );
    }

    @Test
    public void testGetPaymentById_validPaymentType1_returnPaymentDto() {
        when(paymentRepository.findById(500L)).thenReturn(Optional.ofNullable(paymentType1Correct));
        PaymentIdCancellationFeeDTO savedPayment = paymentService.getPaymentById(500L);

        Assertions.assertThat(savedPayment).isNotNull();
        assertEquals(savedPayment.getCancellationFee(), paymentType1Correct.getCancellationFee());
    }

    @Test
    void testCreatePaymentType1_wrongPaymentType_throwWrongPaymentException() {
        WrongPaymentException ex = assertThrows(WrongPaymentException.class, () -> {
            paymentService.createPayment(paymentDtoType1Incorrect);
        });
        assertEquals("Payment of TYPE1 must use currency EUR", ex.getMessage());
    }

    @Test
    void testGetPaymentList_validPayment_returnPaymentListDTO() {
        List<Payment> payments = Arrays.asList(paymentType1Correct);
        when(paymentRepository.findAll()).thenReturn(payments);
        List<PaymentPublicInputDTO> dtos = paymentService.getPaymentList();

        assertEquals(1, dtos.size());
        assertAll("Verify mapping from Entity → DTO",
                () -> assertEquals(paymentType1Correct.getPaymentId(),
                        dtos.get(0).getPaymentId(), "paymentId"),
                () -> assertEquals(paymentType1Correct.getPaymentType(),
                        dtos.get(0).getPaymentType(), "paymentType"),
                () -> assertEquals(paymentType1Correct.getAmount(),
                        dtos.get(0).getAmount(), "amount"),
                () -> assertEquals(paymentType1Correct.getCurrency(),
                        dtos.get(0).getCurrency(), "currency"),
                () -> assertEquals(paymentType1Correct.getDebtorIban(),
                        dtos.get(0).getDebtorIban(), "debtorIban"),
                () -> assertEquals(paymentType1Correct.getCreditorIban(),
                        dtos.get(0).getCreditorIban(), "creditorIban"),
                () -> assertEquals(paymentType1Correct.getDetails(),
                        dtos.get(0).getDetails(), "details")
        );
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllPayments_validPaymentType12_returnPaymentsDtoList() {
        List<Payment> entityList = List.of(paymentType1Correct, paymentType2Correct);
        List<PaymentPublicInputDTO> expectedDtoList = List.of(paymentDtoType1Correct,
                paymentDtoType2Correct);
        when(paymentRepository.findAll()).thenReturn(entityList);

        try (MockedStatic<PaymentMapper> mocked = mockStatic(PaymentMapper.class)) {
            mocked.when(() -> PaymentMapper.toDTO(paymentType1Correct))
                    .thenReturn(paymentDtoType1Correct);
            mocked.when(() -> PaymentMapper.toDTO(paymentType2Correct))
                    .thenReturn(paymentDtoType2Correct);
            List<PaymentPublicInputDTO> result = paymentService.getPaymentList();

            assertEquals(2, result.size());
            assertEquals(1L, result.get(0).getPaymentId());
            assertEquals(2L, result.get(1).getPaymentId());
            mocked.verify(() -> PaymentMapper.toDTO(paymentType1Correct));
            mocked.verify(() -> PaymentMapper.toDTO(paymentType2Correct));
        }
    }

    @Test
    public void testUpdatePayment_validPaymentType1_returnEditedPaymentDto() {
        when(paymentRepository.findById(1L))
                .thenReturn(Optional.ofNullable(paymentType1Correct));
        when(paymentRepository.save(Mockito.any(Payment.class)))
                .thenReturn(paymentType1Correct);
        PaymentPublicInputDTO savedPayment = paymentService
                .updatePayment(1L, paymentDtoType1Correct);

        Assertions.assertThat(savedPayment).isNotNull();
    }
}