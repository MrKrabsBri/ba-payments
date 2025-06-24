package com.jb.payments.mapper;

import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;

public class PaymentMapper {
    public static Payment toEntity(PaymentPublicInputDTO dto) {
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setPaymentType(dto.getPaymentType());
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        payment.setDebtorIban(dto.getDebtorIban());
        payment.setCreditorIban(dto.getCreditorIban());
        payment.setDetails(dto.getDetails());
        payment.setCreditorBankBicCode(dto.getCreditorBankBicCode());
        /*payment.setCancelled(dto.isCanceled());
        payment.setCancellationFee(dto.getCancellationFee());*/

        return payment;
    }

    public static PaymentPublicInputDTO toDTO(Payment payment) {
        PaymentPublicInputDTO dto = new PaymentPublicInputDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setPaymentType(payment.getPaymentType());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setDebtorIban(payment.getDebtorIban());
        dto.setCreditorIban(payment.getCreditorIban());
        dto.setDetails(payment.getDetails());
        dto.setCreditorBankBicCode(payment.getCreditorBankBicCode());
/*        dto.setCanceled(payment.isCancelled());
        dto.setCancellationFee(payment.getCancellationFee());*/

        return dto;
    }
}