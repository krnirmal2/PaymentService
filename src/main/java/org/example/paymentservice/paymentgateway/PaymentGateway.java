package org.example.paymentservice.paymentgateway;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentGateway {
    //NOTE 5: create payment gateway using
    // STRATEGY DESIGN PATTERN
    // choose each design pattern that we need
    String generatePaymentLink(String id, Long amount, String phoneNumber, String orderId) throws RazorpayException, StripeException;

}
