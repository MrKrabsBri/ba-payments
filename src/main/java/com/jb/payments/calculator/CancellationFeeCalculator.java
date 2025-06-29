package com.jb.payments.calculator;

import com.jb.payments.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class CancellationFeeCalculator {

    public static final float COEFFICIENT_TYPE_1 = 0.05f;
    public static final float COEFFICIENT_TYPE_2 = 0.1f;
    public static final float COEFFICIENT_TYPE_3 = 0.15f;

    public static float calculateCancellationFee(Payment payment, long hoursPassed) {

        return switch (payment.getPaymentType()) {
            case TYPE1 -> COEFFICIENT_TYPE_1 * hoursPassed;
            case TYPE2 -> COEFFICIENT_TYPE_2 * hoursPassed;
            case TYPE3 -> COEFFICIENT_TYPE_3 * hoursPassed;
        };
    }

}
