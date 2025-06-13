package com.jb.payments.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidPaymentDetailsFields
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long paymentId;

    private PaymentType paymentType;

    @PositiveOrZero
    private float amount;

    private Currency currency;

    @NotBlank
    private String debtorIban;

    @NotBlank
    private String creditorIban;

    private Float cancellationFee;

    private String detailsForType1;

    private String detailsForType2;

    private String creditorBankBicCode;


}
