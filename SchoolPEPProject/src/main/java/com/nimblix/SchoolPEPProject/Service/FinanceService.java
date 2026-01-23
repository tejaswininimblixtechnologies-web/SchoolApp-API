package com.nimblix.SchoolPEPProject.Service;
import com.nimblix.SchoolPEPProject.Request.FeesPaymentRequest;


public interface FinanceService {

    Object payFees(FeesPaymentRequest request);
    Object getFeesStatus(Long studentId);
    Double getTotalFeesCollected(Long schoolId);
    Double getTotalPendingFees(Long schoolId);
}

