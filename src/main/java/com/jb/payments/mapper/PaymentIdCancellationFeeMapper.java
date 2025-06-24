package com.jb.payments.mapper;

import com.jb.payments.dto.PaymentIdCancellationFeeDTO;
import com.jb.payments.entity.Payment;

public class PaymentIdCancellationFeeMapper {

    public static Payment toEntity(PaymentIdCancellationFeeDTO dto){
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setCancellationFee(dto.getCancellationFee());
        return payment;
    }

    public static PaymentIdCancellationFeeDTO toDTO(Payment payment){
        PaymentIdCancellationFeeDTO paymentActiveInRangeOutputDTO =
                new PaymentIdCancellationFeeDTO();
        paymentActiveInRangeOutputDTO.setPaymentId(payment.getPaymentId());
        paymentActiveInRangeOutputDTO.setCancellationFee(payment.getCancellationFee());
        return paymentActiveInRangeOutputDTO;
    }

}
