package com.jb.payments.validator;

import com.jb.payments.annotation.ValidPaymentDetailsFields;
import com.jb.payments.entity.Payment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static org.apache.logging.log4j.util.Strings.isBlank;

public class PaymentDetailsFieldValidator implements ConstraintValidator<ValidPaymentDetailsFields, Payment> {

    @Override
    public boolean isValid(Payment payment, ConstraintValidatorContext context) {
        if (payment == null || payment.getPaymentType() == null) return true;

        boolean correctDetailsFieldSet = switch (payment.getPaymentType()) {
            case TYPE1 -> isOnlyDetailsFieldSet(payment.getDetailsForType1(), payment.getDetailsForType2(), payment.getCreditorBankBicCode());
            case TYPE2 -> areOtherDetailFieldsEmpty(payment.getDetailsForType1(), payment.getCreditorBankBicCode());
            case TYPE3 -> isOnlyDetailsFieldSet(payment.getCreditorBankBicCode(), payment.getDetailsForType1(), payment.getDetailsForType2());
        };

        return correctDetailsFieldSet;
    }

    private boolean isOnlyDetailsFieldSet(String allowedDetailsField, String... others) {

        if (allowedDetailsField == null || allowedDetailsField.isBlank()) return false;
        for (String s : others) {
            if (s != null ) return false;
        }

        return true;
    }

    private boolean areOtherDetailFieldsEmpty(String... fields) {
        for (String field : fields) {
            if (!isBlank(field)) {
                return false;
            }
        }

        return true;
    }

}
