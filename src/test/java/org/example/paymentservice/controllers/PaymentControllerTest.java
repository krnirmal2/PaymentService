package org.example.paymentservice.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.example.paymentservice.dtos.InitiatePaymentRequestDto;
import org.example.paymentservice.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.RazorpayException;

/**
 * Integration tests for PaymentController.
 * Tests the REST endpoints and request/response handling.
 */
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PaymentService paymentService;
    
    /**
     * Tests successful payment initiation with valid request.
     * Verifies:
     * - HTTP 200 status code
     * - Correct payment link in response
     * - Proper service method invocation
     */
    @Test
    void initiatePayment_ValidRequest_ReturnsPaymentLink() throws Exception {
        // Arrange
        InitiatePaymentRequestDto request = new InitiatePaymentRequestDto("user123", 1000L, "1234567890", "order123");
        String expectedLink = "http://payment.link";
        
        when(paymentService.initiatePayment(  // Fix: Changed from generatePaymentLink to initiatePayment
            request.getEmail(),
            request.getAmount(),
            request.getPhoneNumber(),
            request.getOrderId()
        )).thenReturn(expectedLink);
        
        // Act & Assert
        mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedLink))
            .andExpect(jsonPath("$").value(expectedLink));  // Add: Verify JSON response
    }
    
    /**
     * Tests error handling when payment service fails.
     * Verifies:
     * - HTTP 500 status code
     * - Error message in response
     * - Exception handling
     */
    @Test
    void initiatePayment_ServiceError_ReturnsInternalServerError() throws Exception {
        // Arrange
        InitiatePaymentRequestDto request = new InitiatePaymentRequestDto(
            "user123", 1000L, "1234567890", "order123"
        );
        
        when(paymentService.initiatePayment(  // Fix: Changed from generatePaymentLink to initiatePayment
            anyString(), anyLong(), anyString(), anyString()
        )).thenThrow(new RazorpayException("Payment gateway error"));
        
        // Act & Assert
        mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(containsString("Payment gateway error")));  // Add: Verify error message
    }

    /**
     * Tests validation of invalid request data.
     * Verifies:
     * - HTTP 400 status code
     * - Validation error messages
     */
    @Test
    void initiatePayment_InvalidRequest_ReturnsBadRequest() throws Exception {
        // Arrange
        InitiatePaymentRequestDto request = new InitiatePaymentRequestDto(
            "", -1000L, "", ""  // Invalid data
        );
        
        // Act & Assert
        mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors").exists());  // Verify validation errors
    }
}