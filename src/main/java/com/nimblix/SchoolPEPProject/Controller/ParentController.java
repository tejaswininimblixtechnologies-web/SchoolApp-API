package com.nimblix.SchoolPEPProject.Controller;

import java.util.*;
import java.util.Map;
import com.nimblix.SchoolPEPProject.Request.ParentRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.AuthParentResponse;
import com.nimblix.SchoolPEPProject.Model.Parent;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Service.ParentService;
import com.nimblix.SchoolPEPProject.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parent")
public class ParentController {

    private final ParentService parentService;
    private final NotificationService notificationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthParentResponse> signUp(@RequestBody ParentRegisterRequest request){

        return ResponseEntity.ok(parentService.signUp(request));

    }

    @GetMapping("/profile")
    public ResponseEntity<?> getParentProfile() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        Parent parent = parentService.getParentProfile(email);

        if (parent == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Parent not found"));
        }

        Map<String, Object> response = new HashMap<>();

        response.put("firstName", parent.getFirstName());
        response.put("email", parent.getEmailId());
        response.put("mobile", parent.getMobile());
        response.put("address", parent.getAddress());
        response.put("profilePicture", parent.getProfilePicture());

        List<Map<String, Object>> children = new ArrayList<>();

        for (Student student : parent.getStudents()) {
            Map<String, Object> child = new HashMap<>();
            child.put("studentId", student.getStudentId());
            child.put("name", student.getFirstName() + " " + student.getLastName());
            child.put("classId", student.getClassId());
            child.put("section", student.getSection());
            children.add(child);
        }

        response.put("children", children);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateParentProfile(@RequestBody Map<String, Object> request) {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        Parent updatedParent = parentService.updateParentProfile(email, request);

        if (updatedParent == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Parent not found"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Parent profile updated successfully")
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changeParentPassword(@RequestBody Map<String, Object> request) {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        String oldPassword = (String) request.get("oldPassword");
        String newPassword = (String) request.get("newPassword");

        Parent result = parentService.changeParentPassword(email, oldPassword, newPassword);

        if (result == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Parent not found"));
        }

        if (result.getId() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Old password is incorrect"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Password changed successfully")
        );
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getParentNotifications() {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        Parent parent = parentService.getParentProfile(email);

        return ResponseEntity.ok(
                notificationService.getNotificationsForParent(parent.getId())
        );
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<?> markNotificationRead(@PathVariable Long id) {

        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        Parent parent = parentService.getParentProfile(email);

        notificationService.markNotificationAsRead(id, parent.getId());

        return ResponseEntity.ok(
                Map.of("message", "Notification marked as read")
        );
    }

    @PutMapping("/notifications/read-all")
    public ResponseEntity<?> markAllNotificationsRead() {

        notificationService.markAllAsReadForParent();

        return ResponseEntity.ok(
                Map.of("message", "All notifications marked as read")
        );
    }
}
