package com.nimblix.SchoolPEPProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicTrendModel {

    private String examDate;
    private double averageScore;
    private double passPercentage;
}
