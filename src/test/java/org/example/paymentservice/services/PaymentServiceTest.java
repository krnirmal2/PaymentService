package org.example.paymentservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.paymentservice.paymentgateway.PaymentGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    
    @Mock
    private PaymentGateway paymentGateway;
    
    @InjectMocks
    private PaymentService paymentService;
    
    @Test
    void generatePaymentLink_ValidInput_ReturnsPaymentLink() throws RazorpayException, StripeException {
        // Arrange
        String expectedLink = "http://payment.link";
        when(paymentGateway.generatePaymentLink(
            anyString(), anyLong(), anyString(), anyString())
        ).thenReturn(expectedLink);
        
        // Act
        String actualLink = paymentService.initiatePayment("user123", 1000L, "1234567890", "order123"
        );
        
        // Assert
        assertEquals(expectedLink, actualLink);
        verify(paymentGateway).generatePaymentLink(
            "user123", 1000L, "1234567890", "order123"
        );
    }
    
    @Test
    void generatePaymentLink_GatewayException_ThrowsException() throws RazorpayException, StripeException {
        // Arrange
        when(paymentGateway.generatePaymentLink(
            anyString(), anyLong(), anyString(), anyString())
        ).thenThrow(new RazorpayException("Payment gateway error"));
        
        // Act & Assert
        assertThrows(RazorpayException.class, () -> 
            paymentService.initiatePayment(
                "user123", 1000L, "1234567890", "order123"
            )
        );
    }
}