package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing an instructor's earnings from a course payment.
 */
@Entity
@Table(name = "instructor_earnings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorEarning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be non-negative")
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Platform fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Platform fee must be non-negative")
    @Column(name = "platform_fee", nullable = false)
    private BigDecimal platformFee;

    @NotNull(message = "Net amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Net amount must be non-negative")
    @Column(name = "net_amount", nullable = false)
    private BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "payout_date")
    private LocalDateTime payoutDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum representing the different payout statuses.
     */
    public enum Status {
        PENDING, PROCESSED, PAID
    }

    /**
     * Calculate the net amount based on the amount and platform fee.
     */
    public void calculateNetAmount() {
        if (amount != null && platformFee != null) {
            netAmount = amount.subtract(platformFee);
            if (netAmount.compareTo(BigDecimal.ZERO) < 0) {
                netAmount = BigDecimal.ZERO;
            }
        }
    }

    /**
     * Process the payout.
     * In a real application, this would involve integration with a payment processor.
     *
     * @return true if the payout was successful, false otherwise
     */
    public boolean processPayout() {
        if (this.status != Status.PENDING) {
            return false;
        }

        // In a real application, this would involve calling a payment gateway API
        // For demonstration purposes, we'll simulate a successful payout
        boolean success = true;

        if (success) {
            this.status = Status.PROCESSED;
        }

        return success;
    }

    /**
     * Mark the payout as paid.
     */
    public void markAsPaid() {
        if (this.status == Status.PROCESSED) {
            this.status = Status.PAID;
            this.payoutDate = LocalDateTime.now();
        }
    }
}
