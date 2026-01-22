package com.nimblix.SchoolPEPProject.Service;

public interface FinanceService {

    Double getTotalFeesCollected(Long schoolId);
    Double getTotalPendingFees(Long schoolId);
}

