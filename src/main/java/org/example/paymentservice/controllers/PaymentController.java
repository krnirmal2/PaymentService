package org.example.paymentservice.controllers;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.example.paymentservice.dtos.InitiatePaymentRequestDto;
import org.example.paymentservice.services.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    //NOTE 1 : different api need to expose this to
    // a link should present to link payment gateways


    private PaymentService paymentService;

    //NOTE 12:
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //    @PostMapping("/{order_id}")
    @PostMapping("/")
//    public String initiatePayment(@PathVariable("order_id") String order_id){
    public String initiatePayment(@RequestBody InitiatePaymentRequestDto requestDto) throws RazorpayException, StripeException {
        //NOTE 13:
        // now initiate payment call from the service

        return paymentService.initiatePayment(requestDto.getOrderId(),requestDto.getAmount() ,requestDto.getPhoneNumber(),requestDto.getEmail()
       );


    }
}
