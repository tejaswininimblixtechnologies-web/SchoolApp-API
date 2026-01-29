package com.nimblix.SchoolPEPProject.Model;

import com.nimblix.SchoolPEPProject.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fees_payment")
public class FeesPayment {

    // Unique identifier for each fee payment transaction

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    // Total amount paid in this transaction

    @Column(nullable = false)
    private Double amount;

    // Current status of the payment (PAID, PENDING, FAILED)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
}
