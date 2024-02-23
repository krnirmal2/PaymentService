package org.example.paymentservice.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentGatewayConfig {
    //NOTE 9: this is used to read the value from the
    // application.properties file
    // of razorpay creadential


    @Value("${razorpay.key.id}") // this will read the value of this razorpay.key.id from application.properties file
    private String razorpayId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    //NOTE 10 :
    // now we will return the object of razorpay from here itself instead of RazorpayPaymentGateway.class
    // create method which return RazorpayClient object
    @Bean
    public RazorpayClient createRazorpayClient() throws RazorpayException {
        System.out.printf("id", razorpayId);
        System.out.println(razorpaySecret);
        return new RazorpayClient(razorpayId,razorpaySecret);
    }

}
