package org.example.paymentservice.paymentgateway;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentGateway implements PaymentGateway {

    //NOTE 11: inject the dependecy
    private RazorpayClient razorpayClient;
    // create constructor
    public RazorpayPaymentGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    public String generatePaymentLink(String orderId, Long amount,String phoneNumber,String email) throws RazorpayException {
//        NOTE 8: we have copied the code from the Razor pay Api documentation
//        https://razorpay.com/docs/api/payments/payment-links/create-standard/
//        for generate the payment link for a particular order
//        RazorpayClient razorpay = new RazorpayClient("[YOUR_KEY_ID]", "[YOUR_KEY_SECRET]");
        // SO EVERY TIME WE TRANSACTION THEN EACH TIME THIS razorpay OBJECT WILL CREATE
        // WHICH IS NOT A GOOD IDEA TO CREATE EACH TIME THE OBJECT
        // SOLUTION -- SO WE CAN USE COMMON OBJECT FOR THIS AND USE EACH TRANSACTION
        // razorpayClient is the only one object that is generated from PaymetGatewayConfig.class

        //1. Details of payment
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("accept_partial",true);
//        paymentLinkRequest.put("first_min_partial_amount",100);
        paymentLinkRequest.put("expire_by",1708678345); //Epoch time NEED TO UPDATE EACH TIME the number of seconds that have elapsed since January 1, 1970

        paymentLinkRequest.put("reference_id",orderId);
        paymentLinkRequest.put("description","Payment for policy no #23456");

        // 2.Details of customer
        JSONObject customer = new JSONObject();
        customer.put("name",phoneNumber);
        customer.put("contact","Gaurav Kumar");
        customer.put("email",email);
        paymentLinkRequest.put("customer",customer);

        // 3.Details of notification
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);

        //4.Details of nots of if something need to write during transaction
        JSONObject notes = new JSONObject();
        notes.put("policy_name","Jeevan Bima");
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://example-callback-url.com/");
        paymentLinkRequest.put("callback_method","get");


        //5. Finally create payment link after all the information
        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

        return payment.toString();
    }
}
