package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthParentResponse {
    private boolean success;
    private String message;
    private Object data;
}
