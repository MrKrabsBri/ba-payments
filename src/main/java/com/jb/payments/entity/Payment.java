package com.jb.payments.entity;

import com.jb.payments.enums.Currency;
import com.jb.payments.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long paymentId;

    private PaymentType paymentType;

    // @PositiveOrZero
    private float amount;

    private Currency currency;

    private String debtorIban;

    private String creditorIban;

    private Float cancellationFee;

/*    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;*/


}
