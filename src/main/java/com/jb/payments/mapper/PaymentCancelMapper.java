package com.jb.payments.mapper;

import com.jb.payments.dto.PaymentCancelDTO;
import com.jb.payments.dto.PaymentPublicInputDTO;
import com.jb.payments.entity.Payment;

public class PaymentCancelMapper {
    public static Payment toEntity(PaymentPublicInputDTO dto) {
        Payment payment = new Payment();
        payment.setCancelled(dto.isCancelled());
        payment.setCancellationFee(dto.getCancellationFee());
        return payment;
    }

    public static PaymentCancelDTO toDTO(Payment payment) {
        PaymentCancelDTO paymentCancelDTO = new PaymentCancelDTO();
        paymentCancelDTO.setCancelled(payment.isCancelled());
        return paymentCancelDTO;
    }

}
