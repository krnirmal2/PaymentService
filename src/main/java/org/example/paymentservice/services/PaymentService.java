package org.example.paymentservice.services;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.example.paymentservice.paymentgateway.PaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    //NOTE 14: we need the object of the payment gateway interface with the help of
    // the class which implements it
    private PaymentGateway paymentGateway;
    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    //NOTE 4: from here the service will serve its code to execute
    // initate payment by link to the payment gateways
    public String initiatePayment(String id, Long amount, String phoneNumber, String orderId) throws RazorpayException, StripeException {
//        we should have all the order details to generate the link
//        and we need to integrate all this payment gateways


        // generate the payment link
        return paymentGateway.generatePaymentLink(id, amount, phoneNumber, orderId);
    }
}
