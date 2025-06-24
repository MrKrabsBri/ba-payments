package com.jb.payments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentPublicInputDTO {
    private Long paymentId;

    private PaymentType paymentType;

    private float amount;

    private Currency currency;

    private String debtorIban;

    private String creditorIban;

    private Float cancellationFee;

    private String details;

    private String creditorBankBicCode;

    private boolean canceled;

    private LocalTime timeOfCreation;

    private LocalDate dateOfCreation;

}


