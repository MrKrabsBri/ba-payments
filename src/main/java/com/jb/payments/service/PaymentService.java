package com.jb.payments.service;

import com.jb.payments.dto.PaymentCancelDTO;
import com.jb.payments.dto.PaymentPublicDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.PaymentNotFoundException;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.mapper.PaymentMapper;
import com.jb.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public boolean paymentIsType(Payment payment, PaymentType expectedType) {
        return payment.getPaymentType() == expectedType;
    }

    public PaymentPublicDTO createPayment(PaymentPublicDTO paymentDto) {
        Payment payment = PaymentMapper.toEntity(paymentDto);
        if (paymentIsType(payment, PaymentType.TYPE1) &&
                payment.getCurrency() != Currency.EUR) {
            throw new WrongPaymentException("Payment of TYPE1 must use currency EUR");
        }
        if (payment.getPaymentType() == PaymentType.TYPE1 &&
                (payment.getDetails() == null ||
                        payment.getDetails().isEmpty())) {
            throw new WrongPaymentException("Payment of TYPE1 must have details specified");
        }
        if (paymentIsType(payment, PaymentType.TYPE2) &&
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

    public List<PaymentPublicDTO> getPaymentList() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentPublicDTO> dtoList = new ArrayList<>();
        for (Payment payment : payments) {
            dtoList.add(PaymentMapper.toDTO(payment));
        }

        return dtoList;
    }

    public PaymentPublicDTO getPaymentById(Long paymentId) throws PaymentNotFoundException {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new PaymentNotFoundException("Payment with ID " + paymentId + " does not exist");
        }

        return PaymentMapper.toDTO(payment.get());
    }

    public PaymentPublicDTO updatePayment(Long paymentId, PaymentPublicDTO paymentDto) {
        Payment paymentNew = PaymentMapper.toEntity(paymentDto);

        Payment paymentUpdated = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        paymentUpdated.setPaymentType(paymentNew.getPaymentType());
        paymentUpdated.setCurrency(paymentNew.getCurrency());
        paymentUpdated.setDebtorIban(paymentNew.getDebtorIban());
        paymentUpdated.setCreditorIban(paymentNew.getCreditorIban());
        paymentUpdated.setCancellationFee(paymentNew.getCancellationFee());
        paymentUpdated.setDetails(paymentNew.getDetails());
        paymentUpdated.setCreditorBankBicCode(paymentNew.getCreditorBankBicCode());

        Payment paymentSaved = paymentRepository.save(paymentUpdated);
        return PaymentMapper.toDTO(paymentSaved);
    }

    public PaymentCancelDTO updatePaymentToCancelled(Long paymentId, PaymentCancelDTO paymentCancelDto) {
        return null;
    }

    public void deletePaymentById(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }


}
