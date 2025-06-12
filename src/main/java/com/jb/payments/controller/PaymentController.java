package com.jb.payments.controller;

import com.jb.payments.entity.Payment;
import com.jb.payments.error.PaymentNotFoundException;
import com.jb.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloWorld(){
        return "Welcome to payment application!";
    }

    @PostMapping("/payment")
    public Payment createPayment(@Valid @RequestBody Payment payment){
        LOGGER.info("Creating a payment...");

        return paymentService.createPayment(payment);
    }

    @GetMapping("/payments")
    public List<Payment> getPaymentList(){
        LOGGER.info((("Getting a list of payments...")));

        return paymentService.getPaymentList();
    }

    @GetMapping("/payment/{id}")
    public Payment getPaymentById(@PathVariable("id") Long paymentId) throws PaymentNotFoundException {
        LOGGER.info((("Retrieving a payment with ID " + paymentId)));

        return paymentService.getPaymentById(paymentId);
    }

    @PutMapping("/payment/{id}")
    public Payment updatePayment
            (@PathVariable("id") Long paymentId,@RequestBody Payment payment){
        LOGGER.info((("Updating a payment with ID " + paymentId)));

        return paymentService.updatePayment(paymentId,payment);
    }

    @DeleteMapping("/payment/{id}")
    public String deletePaymentById(@PathVariable("id") Long paymentId){
        paymentService.deletePaymentById(paymentId);
        LOGGER.info((("Deleting payment with ID " + paymentId)));

        return "Payment ID no. " + paymentId + " deleted Successfully";
    }
}
