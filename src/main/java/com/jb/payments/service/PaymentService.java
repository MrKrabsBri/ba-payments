package com.jb.payments.service;

import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.PaymentNotFoundException;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public boolean paymentIsType(Payment payment, PaymentType expectedType){
        return payment.getPaymentType() == expectedType;
    }

    public Payment createPayment(Payment payment) {

        if (paymentIsType(payment,PaymentType.TYPE1) &&
                payment.getCurrency() != Currency.EUR) {
            throw new WrongPaymentException("Payment of TYPE1 must use currency EUR");
        }
        if (payment.getPaymentType() == PaymentType.TYPE1 &&
                (payment.getDetailsForType1() == null ||
                        payment.getDetailsForType1().isEmpty())) {
            throw new WrongPaymentException("Payment of TYPE1 must have details specified");
        }

        if (paymentIsType(payment,PaymentType.TYPE2) &&
                payment.getCurrency() != Currency.USD) {
            throw new WrongPaymentException("Payment of TYPE2 must use currency USD");
        }

        if (payment.getPaymentType() == PaymentType.TYPE3 &&
                (payment.getCreditorBankBicCode() == null ||
                        payment.getCreditorBankBicCode().isEmpty())) {
            throw new WrongPaymentException("Payment of TYPE3 must have creditor bank BIC code specified");
        }


        return paymentRepository.save(payment);


    }

    public List<Payment> getPaymentList() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long paymentId) throws PaymentNotFoundException {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new PaymentNotFoundException("Payment with ID " + paymentId + " does not exist");
        }

        return payment.get();
    }

    public Payment updatePayment(Long paymentId, Payment payment) {
        Payment paymentUpdated = paymentRepository.findById(paymentId).get();
        if (Objects.nonNull(payment.getPaymentType()) &&
                !"".equalsIgnoreCase(payment.getPaymentType().toString())) {

            paymentUpdated.setPaymentType(payment.getPaymentType());
        }

        return paymentRepository.save(paymentUpdated);
    }

    public void deletePaymentById(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
