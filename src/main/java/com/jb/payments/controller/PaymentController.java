package com.jb.payments.controller;

import com.jb.payments.dto.PaymentIdCancellationFeeDTO;
import com.jb.payments.dto.PaymentIdDTO;
import com.jb.payments.dto.PaymentCancelDTO;
import com.jb.payments.dto.PaymentPublicInputDTO;
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
    public ResponseEntity<PaymentPublicInputDTO> createPayment
            (@Valid @RequestBody PaymentPublicInputDTO paymentDto){
        PaymentPublicInputDTO savedPayment = paymentService.createPayment(paymentDto);
        LOGGER.info("Creating a payment...");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPayment);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentPublicInputDTO>> getPaymentList(){
        LOGGER.info((("Getting a list of payments...")));
        List<PaymentPublicInputDTO> paymentList = paymentService.getPaymentList();

        return ResponseEntity.ok(paymentList);
    }

    @GetMapping("/payment/plain/{id}")
    public ResponseEntity<PaymentPublicInputDTO>
    getPaymentByIdForTesting(@PathVariable("id") Long paymentId) throws PaymentNotFoundException {
        LOGGER.info("Retrieving a payment with ID {}", paymentId);
        PaymentPublicInputDTO paymentDto = paymentService.getPaymentByIdForTesting(paymentId);

        return ResponseEntity.ok(paymentDto);
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<PaymentPublicInputDTO> updatePayment
            (@PathVariable("id") Long paymentId,@RequestBody PaymentPublicInputDTO paymentDto){
        LOGGER.info("Updating a payment with ID {}", paymentId);
        PaymentPublicInputDTO updatedPayment = paymentService.updatePayment(paymentId,paymentDto);

        return ResponseEntity.ok(updatedPayment);
    }

    @PutMapping("/payment/cancel/{id}")
    public ResponseEntity<PaymentCancelDTO> updatePaymentToCancelled
            (@PathVariable("id") Long paymentId/*,@RequestBody PaymentCancelDTO paymentCancelDto*/){
        LOGGER.info("Updating a payment with ID {} to canceled", paymentId);
        PaymentCancelDTO updatedPayment = paymentService.updatePaymentToCancelled(paymentId);

        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/active")
    public ResponseEntity<List<PaymentIdDTO>> getActivePayments() {
        List <PaymentIdDTO> dtos = paymentService.getActivePayments();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/active/range")
    public ResponseEntity<List<PaymentIdDTO>> listPaymentsInRange(
            @RequestParam("min") float min, @RequestParam("max") float max) {
        List<PaymentIdDTO> dtos = paymentService.getActivePaymentsInRange(min, max);

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<PaymentIdCancellationFeeDTO> getPaymentById(@PathVariable("id") Long paymentId) throws PaymentNotFoundException {
        LOGGER.info("Retrieving a payment with ID {}", paymentId);
        PaymentIdCancellationFeeDTO paymentDto = paymentService.getPaymentById(paymentId);

        return ResponseEntity.ok(paymentDto);
    }
}
