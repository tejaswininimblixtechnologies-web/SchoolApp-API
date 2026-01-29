package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Repository.FeesPaymentRepository;
import com.nimblix.SchoolPEPProject.Service.FinanceService;
import com.nimblix.SchoolPEPProject.Enum.PaymentStatus;
import com.nimblix.SchoolPEPProject.Request.FeesPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final FeesPaymentRepository feesPaymentRepository;

    @Override
    public Double getTotalPendingFees(Long schoolId) {
        Double assigned = feesPaymentRepository.getTotalAssignedFees(schoolId);
        Double paid = feesPaymentRepository.getTotalPaidFees(
                schoolId,
                PaymentStatus.PAID
        );
        return assigned - paid;
    }

    @Override
    public Double getTotalFeesCollected(Long schoolId) {

        // will implement later

        return 0.0;
    }

    @Override
    public Object payFees(FeesPaymentRequest request) {

        // will implement later

        return null;
    }

    @Override
    public Object getFeesStatus(Long studentId) {

        // will implement later

        return null;
    }
}
