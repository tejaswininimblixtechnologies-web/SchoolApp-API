package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Repository.SchoolRepository;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;
import com.nimblix.SchoolPEPProject.Service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    @Override
    public School registerSchool(SchoolRegistrationRequest request) {

        if (request.getSchoolName() == null || request.getSchoolName().isBlank()) {
            throw new IllegalArgumentException("School name is mandatory");
        }

        if (schoolRepository.existsBySchoolEmail(request.getSchoolEmail())) {
            throw new RuntimeException("School already registered with this email");
        }

        // 🔹 Determine location type
        String locationType = "MANUAL";
        if (request.getLatitude() != null && request.getLongitude() != null) {
            locationType = "GPS";
        }

        School school = School.builder()
                .schoolName(request.getSchoolName())
                .schoolAddress(request.getSchoolAddress())
                .schoolPhone(request.getSchoolPhone())
                .schoolEmail(request.getSchoolEmail())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .locationType(locationType)
                .build();

        return schoolRepository.save(school);
    }

}
