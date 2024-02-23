package org.example.paymentservice.paymentgateway;

import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.LineItem;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import lombok.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Service
public class StripePaymentGateway implements  PaymentGateway{

//    @Value("${STRIPE_KEY_SECRET}")
    private String stripeKey;
    @Override
    public String generatePaymentLink(String id, Long amount, String phoneNumber, String orderId) throws StripeException {
        //NOTE 15: we just copy the strip payment link generation code
        Stripe.apiKey = "sk_test_tR3PYbcVNZZ796tH88S4VQ2u";
/*

        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder() // Note 16: Builder pattern
                                        .setPrice("price_1MoC3TLkdIwHu7ixcIbKelAC")// This is an price object not a integral value
                                        .setQuantity(1L)
                                        .build()
                        )
                        .build();
*/

//            Stripe.apiKey = stripeKey;
            Map<String, Object> priceData = new HashMap<>();
            priceData.put("unit_amount", amount);
            priceData.put("currency", "inr");

            Map<String, Object> productData = new HashMap<>();
            productData.put("name", "iPhone");
            //productData.put("quantity", 2);
            priceData.put("product_data", productData);

            Price price = Price.create(priceData);

            //params.put("line_items", );
            Map<String, Object> lineItem1 = new HashMap<>();
            lineItem1.put("price", price.getId());
            lineItem1.put("quantity", 5);

            Map<String, Object> afterPayment = new HashMap<>();
            afterPayment.put("type", "redirect");

            Map<String, Object> redirect = new HashMap<>(); //lineItem2
            redirect.put("url", "https://scaler.com/");

            afterPayment.put("redirect", redirect);

            List<Object> lineItems = new ArrayList<>();
            lineItems.add(lineItem1);

            Map<String, Object> params = new HashMap<>();
            params.put("line_items", lineItems);
            params.put("after_completion", afterPayment);
            //params.put("generate_invoice", map);

            PaymentLink paymentLink = PaymentLink.create(params);

            return paymentLink.getUrl();
        }
}
