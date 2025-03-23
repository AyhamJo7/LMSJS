package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a student's progress for a specific course content.
 */
@Entity
@Table(name = "content_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private CourseContent content;

    @Column(name = "is_completed")
    private boolean isCompleted = false;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Mark this content as accessed by updating the last accessed time.
     */
    public void markAsAccessed() {
        this.lastAccessedAt = LocalDateTime.now();
    }

    /**
     * Mark this content as completed.
     */
    public void markAsCompleted() {
        if (!this.isCompleted) {
            this.isCompleted = true;
            this.completedAt = LocalDateTime.now();
            this.progressPercentage = new BigDecimal("100.0");
            
            // Recalculate enrollment progress
            if (enrollment != null) {
                enrollment.calculateProgress();
            }
        }
    }

    /**
     * Update the progress percentage.
     *
     * @param percentage the new progress percentage (0-100)
     */
    public void updateProgress(BigDecimal percentage) {
        if (percentage.compareTo(BigDecimal.ZERO) < 0) {
            percentage = BigDecimal.ZERO;
        } else if (percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            percentage = BigDecimal.valueOf(100);
        }

        this.progressPercentage = percentage;
        this.lastAccessedAt = LocalDateTime.now();

        if (percentage.compareTo(BigDecimal.valueOf(100)) == 0) {
            markAsCompleted();
        }

        // Recalculate enrollment progress
        if (enrollment != null) {
            enrollment.calculateProgress();
        }
    }
}
