package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Admin entity extending base User entity.
 *
 * Inherits common profile fields from User:
 * - firstName, lastName
 * - emailId (unique)
 * - mobile
 * - profilePicture
 * - status (ACTIVE / DELETED)
 * - createdTime, updatedTime
 * - hasNewNotification (notification bell indicator)
 *
 * Business-specific field:
 * - adminId (unique identifier for Admin)
 *
 * Soft delete is supported via:
 * status = DELETED
 */

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id", unique = true)
    private String adminId;
}
