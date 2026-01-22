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
    public Double getTotalFeesCollected(Long schoolId) {
        if (schoolId == null) {
            throw new IllegalArgumentException("School ID is required");
        }
        return feesPaymentRepository.getTotalCollectedFees(schoolId);
    }
}
