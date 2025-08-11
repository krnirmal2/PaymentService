package org.example.paymentservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
public class InitiatePaymentRequestDto {
    //NOTE 3: to make a payment we need to take order Id ,amount , phone number,email which we will pass
    private String orderId;
    private Long amount;
    private String phoneNumber;
    private String email;

}
