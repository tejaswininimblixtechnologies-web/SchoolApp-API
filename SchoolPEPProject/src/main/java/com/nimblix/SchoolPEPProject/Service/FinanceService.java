package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Request.FeesPaymentRequest;

public interface FinanceService {

// totallfinace details
    Double getTotalPendingFees(Long schoolId);


    Double getTotalFeesCollected(Long schoolId);

    Object payFees(FeesPaymentRequest request);

    Object getFeesStatus(Long studentId);
}
