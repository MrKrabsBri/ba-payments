package com.jb.payments.entity;

import com.jb.payments.annotation.ValidPaymentDetailsFields;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidPaymentDetailsFields
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private PaymentType paymentType;

    @PositiveOrZero
    private float amount;

    private Currency currency;

    @NotBlank
    private String debtorIban;

    @NotBlank
    private String creditorIban;

    private String details;

    private String creditorBankBicCode;

    private float cancellationFee;

    private boolean isCancelled;

    @CreationTimestamp
    private LocalTime timeOfCreation;

    @CreationTimestamp
    private LocalDate dateOfCreation;
}
