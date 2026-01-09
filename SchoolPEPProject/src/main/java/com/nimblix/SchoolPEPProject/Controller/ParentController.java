package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Request.ParentRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.AuthParentResponse;
import com.nimblix.SchoolPEPProject.Service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parent")
public class ParentController {

    private final ParentService parentService;

    @PostMapping("/signup")
    public ResponseEntity<AuthParentResponse> signUp(@RequestBody ParentRegisterRequest request){

        return ResponseEntity.ok(parentService.signUp(request));

    }

}
