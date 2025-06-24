package com.jb.payments.mapper;

import com.jb.payments.dto.PaymentIdDTO;
import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;

public class PaymentIdMapper {

    public static Payment toEntity(PaymentPublicInputDTO dto){
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        return payment;
    }

    public static PaymentIdDTO toDTO(Payment payment){
        PaymentIdDTO paymentActiveInRangeOutputDTO = new PaymentIdDTO();
        paymentActiveInRangeOutputDTO.setPaymentId(payment.getPaymentId());
        return paymentActiveInRangeOutputDTO;
    }
}
