package com.nimblix.SchoolPEPProject.Request;

import lombok.Data;

@Data
public class FeesPaymentRequest {

    private Long studentId;
    private Long schoolId;
    private Double amount;
    private String paymentMode;
}
