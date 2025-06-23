package com.jb.payments.service;

import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import com.jb.payments.error.PaymentNotFoundException;
import com.jb.payments.error.WrongPaymentException;
import com.jb.payments.mapper.PaymentMapper;
import com.jb.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final float COEFFICIENT_TYPE_1 = 0.05f;
    private final float COEFFICIENT_TYPE_2 = 0.1f;
    private final float COEFFICIENT_TYPE_3 = 0.15f;

    @Autowired
    private PaymentRepository paymentRepository;


    public boolean paymentIsType(Payment payment, PaymentType expectedType) {
        return payment.getPaymentType() == expectedType;
    }

    public PaymentPublicInputDTO createPayment(PaymentPublicInputDTO paymentDto) {
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

    public List<PaymentPublicInputDTO> getPaymentList() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentPublicInputDTO> dtoList = new ArrayList<>();
        for (Payment payment : payments) {
            dtoList.add(PaymentMapper.toDTO(payment));
        }

        return dtoList;
    }

    public PaymentPublicInputDTO getPaymentById(Long paymentId) throws PaymentNotFoundException {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new PaymentNotFoundException("Payment with ID " + paymentId + " does not exist");
        }

        return PaymentMapper.toDTO(payment.get());
    }

    public PaymentPublicInputDTO updatePayment(Long paymentId, PaymentPublicInputDTO paymentDto) {
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

    public PaymentPublicInputDTO updatePaymentToCancelled(Long paymentId) {
        //Payment paymentNew = PaymentCancelMapper.toEntity(payment);
        Payment paymentDB = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

/*        LocalDateTime now = LocalDateTime.now();
        LocalDateTime creationDateTime = LocalDateTime.of(
                paymentDB.getDateOfCreation(),
                paymentDB.getTimeOfCreation()
        );*/


        if (nowDate.equals(paymentDB.getDateOfCreation()) &&
                (nowTime.isAfter(paymentDB.getTimeOfCreation()) || nowTime.equals(paymentDB.getTimeOfCreation()))) {

            Duration duration = Duration.between(paymentDB.getTimeOfCreation(), nowTime);
            long hoursPassed = duration.toHours();

            paymentDB.setCancelled(true);

            switch (paymentDB.getPaymentType()) {
                case TYPE1 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_1 * hoursPassed);
                case TYPE2 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_2 * hoursPassed);
                case TYPE3 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_3 * hoursPassed);
            }

            Payment saved = paymentRepository.save(paymentDB);
            return PaymentMapper.toDTO(saved);
        }
        else throw new IllegalStateException("Cancellation attempted after 00:00");
/*        if (!now.isBefore(creationDateTime)) {
            Duration timePassed = Duration.between(creationDateTime, now);
            long hoursPassed = timePassed.toHours();

            paymentDB.setCancelled(true);

            switch (paymentDB.getPaymentType()) {
                case TYPE1 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_1 * hoursPassed);
                case TYPE2 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_2 * hoursPassed);
                case TYPE3 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_3 * hoursPassed);
            }

            Payment savedPayment = paymentRepository.save(paymentDB);
            return PaymentMapper.toDTO(savedPayment);
        } else {
            throw new IllegalStateException("Cancellation attempted before payment creation time.");
        }*/




       /* if (nowDate.getYear() == paymentDB.getDateOfCreation().getYear() &&
                nowDate.getDayOfYear() == paymentDB.getDateOfCreation().getDayOfYear() &&
                    (nowTime.isAfter(paymentDB.getTimeOfCreation()) ||
                        nowTime.equals(paymentDB.getTimeOfCreation()))) {

            Duration timePassedSinceCreation = Duration.between(paymentDB.getTimeOfCreation(), nowTime);
            long hoursPassedSinceCreation = timePassedSinceCreation.toHours();

            paymentDB.setCancelled(true);

           switch (paymentDB.getPaymentType()) {
                case TYPE1 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_1 * hoursPassedSinceCreation);
                case TYPE2 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_2 * hoursPassedSinceCreation);
                case TYPE3 -> paymentDB.setCancellationFee(COEFFICIENT_TYPE_3 * hoursPassedSinceCreation);
            };

            Payment paymentSaved = paymentRepository.save(paymentDB);
            return PaymentMapper.toDTO(paymentSaved);

        }

        return PaymentMapper.toDTO(paymentDB);
*/
    }

    public void deletePaymentById(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }


}
