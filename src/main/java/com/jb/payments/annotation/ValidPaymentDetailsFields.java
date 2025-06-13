package com.jb.payments.annotation;

import com.jb.payments.validator.PaymentDetailsFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentDetailsFieldValidator.class)
public @interface ValidPaymentDetailsFields {
    String message() default "Only the appropriate details field must be filled based on payment type.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
