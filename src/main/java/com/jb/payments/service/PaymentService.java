package com.jb.payments.service;

import com.jb.payments.calculator.CancellationFeeCalculator;
import com.jb.payments.dto.PaymentIdCancellationFeeDTO;
import com.jb.payments.dto.PaymentIdDTO;
import com.jb.payments.dto.PaymentCancelDTO;
import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.PaymentNotFoundException;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.mapper.PaymentCancelMapper;
import com.jb.payments.mapper.PaymentIdCancellationFeeMapper;
import com.jb.payments.mapper.PaymentMapper;
import com.jb.payments.mapper.PaymentIdMapper;
import com.jb.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private Clock clock;
    @Autowired
    private CancellationFeeCalculator cancellationFeeCalculator;

    public PaymentPublicInputDTO createPayment(PaymentPublicInputDTO paymentDto) {
        Payment payment = PaymentMapper.toEntity(paymentDto);
        if (payment.getPaymentType() == PaymentType.TYPE1 &&
                payment.getCurrency() != Currency.EUR) {
            throw new WrongPaymentException("Payment of TYPE1 must use currency EUR");
        }
        if (payment.getPaymentType() == PaymentType.TYPE1 &&
                (payment.getDetails() == null ||
                        payment.getDetails().isEmpty())) {
            throw new WrongPaymentException("Payment of TYPE1 must have details specified");
        }
        if (payment.getPaymentType() == PaymentType.TYPE2 &&
                payment.getCurrency() != Currency.USD) {
            throw new WrongPaymentException("Payment of TYPE2 must use currency USD");
        }
        if (payment.getPaymentType() == PaymentType.TYPE3 &&
                (payment.getCreditorBankBicCode() == null ||
                        payment.getCreditorBankBicCode().isEmpty())) {
            throw new WrongPaymentException("Payment of TYPE3 must have creditor bank BIC code specified");
        }
        paymentRepository.save(payment);

        return PaymentMapper.toDTO(payment);
    }

    public List<PaymentPublicInputDTO> getPaymentList() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaymentPublicInputDTO getPaymentByIdForTesting(Long paymentId) throws PaymentNotFoundException {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty()) {
            throw new PaymentNotFoundException("Payment with ID " + paymentId + " does not exist");
        }

        return PaymentMapper.toDTO(payment.get());
    }

    public PaymentPublicInputDTO updatePayment(Long paymentId, PaymentPublicInputDTO paymentDto) {
        Payment paymentNew = PaymentMapper.toEntity(paymentDto);
        Payment paymentUpdated = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
        paymentUpdated.setPaymentType(paymentNew.getPaymentType());
        paymentUpdated.setAmount(paymentNew.getAmount());
        paymentUpdated.setCurrency(paymentNew.getCurrency());
        paymentUpdated.setDebtorIban(paymentNew.getDebtorIban());
        paymentUpdated.setCreditorIban(paymentNew.getCreditorIban());
        paymentUpdated.setCancellationFee(paymentNew.getCancellationFee());
        paymentUpdated.setDetails(paymentNew.getDetails());
        paymentUpdated.setCreditorBankBicCode(paymentNew.getCreditorBankBicCode());
        Payment paymentSaved = paymentRepository.save(paymentUpdated);

        return PaymentMapper.toDTO(paymentSaved);
    }

    public PaymentCancelDTO updatePaymentToCancelled(Long paymentId) {
        Payment paymentDB = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        LocalDate nowDate = LocalDate.now(clock);
        LocalTime nowTime = LocalTime.now(clock);

        if (nowDate.equals(paymentDB.getDateOfCreation()) &&
                (nowTime.isAfter(paymentDB.getTimeOfCreation()) ||
                        nowTime.equals(paymentDB.getTimeOfCreation()))) {

            Duration duration = Duration.between(paymentDB.getTimeOfCreation(), nowTime);
            long hoursPassed = duration.toHours();

            paymentDB.setCancelled(true);
            paymentDB.setCancellationFee(cancellationFeeCalculator
                    .calculateCancellationFee(paymentDB, hoursPassed));
            Payment saved = paymentRepository.save(paymentDB);
            return PaymentCancelMapper.toDTO(saved);
        } else throw new IllegalStateException("Cancellation attempted after 00:00");
    }

    public List<PaymentIdDTO> getActivePayments() {
        return paymentRepository.findAllByCancelledFalse()
                .stream()
                .map(PaymentIdMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentIdDTO> getActivePaymentsInRange(float min, float max) {
        return paymentRepository.findAllByCancelledFalseAndAmountBetween(min, max).stream()
                .map(PaymentIdMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaymentIdCancellationFeeDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment with ID " + paymentId + " does not exist")
                );

        return PaymentIdCancellationFeeMapper.toDTO(payment);
    }
}
