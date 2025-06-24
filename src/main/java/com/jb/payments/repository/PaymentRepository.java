package com.jb.payments.repository;

import com.jb.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findAllByCancelledFalse();
    List<Payment> findAllByCancelledFalseAndAmountBetween(float min, float max);
}
