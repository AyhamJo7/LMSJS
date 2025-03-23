package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a payment for a course enrollment.
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be non-negative")
    @Column(nullable = false)
    private BigDecimal amount;

    @Size(max = 3, message = "Currency code must be 3 characters")
    private String currency = "USD";

    @Size(max = 50, message = "Payment method must be less than 50 characters")
    @Column(name = "payment_method")
    private String paymentMethod;

    @Size(max = 255, message = "Transaction ID must be less than 255 characters")
    @Column(name = "transaction_id")
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InstructorEarning> instructorEarnings = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum representing the different payment statuses.
     */
    public enum Status {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    /**
     * Process the payment.
     * In a real application, this would involve integration with a payment processor.
     *
     * @return true if the payment was successful, false otherwise
     */
    public boolean processPayment() {
        // In a real application, this would involve calling a payment gateway API
        // For demonstration purposes, we'll simulate a successful payment
        boolean success = true;

        if (success) {
            this.status = Status.COMPLETED;
            this.paymentDate = LocalDateTime.now();
        } else {
            this.status = Status.FAILED;
        }

        return success;
    }

    /**
     * Refund the payment.
     * In a real application, this would involve integration with a payment processor.
     *
     * @return true if the refund was successful, false otherwise
     */
    public boolean refundPayment() {
        if (this.status != Status.COMPLETED) {
            return false;
        }

        // In a real application, this would involve calling a payment gateway API
        // For demonstration purposes, we'll simulate a successful refund
        boolean success = true;

        if (success) {
            this.status = Status.REFUNDED;
        }

        return success;
    }

    /**
     * Add an instructor earning to this payment.
     *
     * @param instructorEarning the instructor earning to add
     */
    public void addInstructorEarning(InstructorEarning instructorEarning) {
        instructorEarnings.add(instructorEarning);
        instructorEarning.setPayment(this);
    }

    /**
     * Remove an instructor earning from this payment.
     *
     * @param instructorEarning the instructor earning to remove
     */
    public void removeInstructorEarning(InstructorEarning instructorEarning) {
        instructorEarnings.remove(instructorEarning);
        instructorEarning.setPayment(null);
    }
}
