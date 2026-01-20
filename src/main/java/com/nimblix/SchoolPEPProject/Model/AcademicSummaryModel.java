package com.nimblix.SchoolPEPProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSummaryModel {

    private long totalExams;
    private double averagePerformancePercentage;
    private int highestScore;
    private int lowestScore;
}
