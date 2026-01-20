package com.nimblix.SchoolPEPProject.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeTrendModel {

    private String date;
    private double totalAmount;
    private double paidAmount;
    private double pendingAmount;
}
