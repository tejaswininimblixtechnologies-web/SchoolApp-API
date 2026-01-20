package com.nimblix.SchoolPEPProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeSummaryModel {

    private double totalFee;
    private double totalPaid;
    private double totalPending;
}
