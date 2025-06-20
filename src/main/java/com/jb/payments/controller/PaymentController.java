package com.jb.payments.controller;

import com.jb.payments.dto.PaymentPublicDTO;
import com.jb.payments.error.PaymentNotFoundException;
import com.jb.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PaymentPublicDTO> createPayment(@Valid @RequestBody PaymentPublicDTO paymentDto){
        PaymentPublicDTO savedPayment = paymentService.createPayment(paymentDto);
        LOGGER.info("Creating a payment...");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPayment);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentPublicDTO>> getPaymentList(){
        LOGGER.info((("Getting a list of payments...")));
        List<PaymentPublicDTO> paymentList = paymentService.getPaymentList();
        return ResponseEntity.ok(paymentList);
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<PaymentPublicDTO>
    getPaymentById(@PathVariable("id") Long paymentId) throws PaymentNotFoundException {
        LOGGER.info("Retrieving a payment with ID {}", paymentId);
        PaymentPublicDTO paymentDto = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(paymentDto);
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<PaymentPublicDTO> updatePayment
            (@PathVariable("id") Long paymentId,@RequestBody PaymentPublicDTO paymentDto){
        LOGGER.info("Updating a payment with ID {}", paymentId);
        PaymentPublicDTO updatedPayment = paymentService.updatePayment(paymentId,paymentDto);

        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/payment/{id}")
    public ResponseEntity<String> deletePaymentById(@PathVariable("id") Long paymentId){
        paymentService.deletePaymentById(paymentId);
        LOGGER.info("Deleting payment with ID {}", paymentId);

        return ResponseEntity.ok("Payment nr. " + paymentId + " deleted Successfully.");
    }
}
