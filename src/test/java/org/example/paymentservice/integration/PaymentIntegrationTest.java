package org.example.paymentservice.integration;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.example.paymentservice.dtos.InitiatePaymentRequestDto;
import org.example.paymentservice.paymentgateway.PaymentGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PaymentGateway paymentGateway;
    
    @Test
    void completePaymentFlow_Success() throws Exception {
        // Arrange
        String expectedLink = "http://payment.link";
        InitiatePaymentRequestDto request = new InitiatePaymentRequestDto("user123", 1000L, "1234567890", "order123");
        
        when(paymentGateway.generatePaymentLink(
            anyString(), anyLong(), anyString(), anyString()
        )).thenReturn(expectedLink);
        
        // Act & Assert
        mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedLink));
    }
}