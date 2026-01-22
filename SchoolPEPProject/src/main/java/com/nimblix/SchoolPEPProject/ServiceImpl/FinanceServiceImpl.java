package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Repository.FeesPaymentRepository;
import com.nimblix.SchoolPEPProject.Service.FinanceService;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceImpl implements FinanceService {

    private final FeesPaymentRepository feesPaymentRepository;

    public FinanceServiceImpl(FeesPaymentRepository feesPaymentRepository) {
        this.feesPaymentRepository = feesPaymentRepository;
    }

    @Override
    public Double getTotalPendingFees(Long schoolId) {
        Double assigned = feesPaymentRepository.getTotalAssignedAmount(schoolId);
        Double paid = feesPaymentRepository.getTotalPaidAmount(schoolId);
        return assigned - paid;
    }

    @Override
    public Double getTotalFeesCollected(Long schoolId) {
        return feesPaymentRepository.getTotalPaidAmount(schoolId);
    }
}

