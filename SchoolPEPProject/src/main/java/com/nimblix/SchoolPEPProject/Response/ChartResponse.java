package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponse {
    private List<String> labels;
    private List<Double> data;
}
