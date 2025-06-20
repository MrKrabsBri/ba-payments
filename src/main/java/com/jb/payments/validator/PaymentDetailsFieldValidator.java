package com.jb.payments.validator;

import com.jb.payments.annotation.ValidPaymentDetailsFields;
import com.jb.payments.entity.Payment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PaymentDetailsFieldValidator implements ConstraintValidator<ValidPaymentDetailsFields, Payment> {

    @Override
    public boolean isValid(Payment payment, ConstraintValidatorContext context) {
        if (payment == null || payment.getPaymentType() == null) return true;

        return switch (payment.getPaymentType()) {
            case TYPE1 -> isOnlyDetailsFieldSet(payment.getDetails(), payment.getCreditorBankBicCode());
            case TYPE2 -> areOtherDetailFieldsEmpty(payment.getDetails(), payment.getCreditorBankBicCode());
            case TYPE3 -> isOnlyDetailsFieldSet(payment.getCreditorBankBicCode(), payment.getDetails());
        };

    }

    private boolean isOnlyDetailsFieldSet(String details, String creditorDetails) {
        if (details == null || details.isBlank()) return false;

        return creditorDetails == null;
    }

    private boolean areOtherDetailFieldsEmpty(String details, String creditorDetails) {
        if (details != null && details.isBlank()) return false;

        return creditorDetails == null;
    }

}
