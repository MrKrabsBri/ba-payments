package com.jb.payments.mapper;

import com.jb.payments.dto.PaymentPublicDTO;
import com.jb.payments.entity.Payment;

public class PaymentMapper {
    public static Payment toEntity(PaymentPublicDTO dto) {
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setPaymentType(dto.getPaymentType());
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        payment.setDebtorIban(dto.getDebtorIban());
        payment.setCreditorIban(dto.getCreditorIban());
        payment.setDetails(dto.getDetails());
        payment.setCreditorBankBicCode(dto.getCreditorBankBicCode());

        return payment;
    }

    public static PaymentPublicDTO toDTO(Payment payment) {
        PaymentPublicDTO dto = new PaymentPublicDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setPaymentType(payment.getPaymentType());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setDebtorIban(payment.getDebtorIban());
        dto.setCreditorIban(payment.getCreditorIban());
        dto.setDetails(payment.getDetails());
        dto.setCreditorBankBicCode(payment.getCreditorBankBicCode());

        return dto;
    }
}