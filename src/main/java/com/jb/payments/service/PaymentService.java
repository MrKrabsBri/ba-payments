package com.jb.payments.service;

import com.jb.payments.entity.Payment;
import com.jb.payments.error.PaymentNotFoundException;
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

    public Payment createPayment(Payment payment){
        return paymentRepository.save(payment);
    }

    public List<Payment>getPaymentList(){
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long paymentId) throws PaymentNotFoundException{
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if(!payment.isPresent()){
            throw new PaymentNotFoundException("Payment with ID " +paymentId + " does not exist");
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
