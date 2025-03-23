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
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a student's enrollment in a course.
 */
@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate = LocalDateTime.now();

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    @Column(name = "is_completed")
    private boolean isCompleted = false;

    @Column(name = "certificate_issued")
    private boolean certificateIssued = false;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Certificate certificate;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CourseReview review;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentProgress> contentProgresses = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Calculate the current progress percentage based on content progress.
     */
    public void calculateProgress() {
        if (contentProgresses.isEmpty()) {
            this.progressPercentage = BigDecimal.ZERO;
            return;
        }

        long completedContents = contentProgresses.stream()
                .filter(ContentProgress::isCompleted)
                .count();

        if (completedContents == 0) {
            this.progressPercentage = BigDecimal.ZERO;
            return;
        }

        BigDecimal percentage = new BigDecimal(completedContents)
                .multiply(BigDecimal.valueOf(100))
                .divide(new BigDecimal(contentProgresses.size()), 2, BigDecimal.ROUND_HALF_UP);

        this.progressPercentage = percentage;
        this.isCompleted = percentage.compareTo(BigDecimal.valueOf(100)) == 0;

        if (this.isCompleted && this.completionDate == null) {
            this.completionDate = LocalDateTime.now();
        }
    }

    /**
     * Add a content progress record to this enrollment.
     *
     * @param contentProgress the content progress to add
     */
    public void addContentProgress(ContentProgress contentProgress) {
        contentProgresses.add(contentProgress);
        contentProgress.setEnrollment(this);
    }

    /**
     * Remove a content progress record from this enrollment.
     *
     * @param contentProgress the content progress to remove
     */
    public void removeContentProgress(ContentProgress contentProgress) {
        contentProgresses.remove(contentProgress);
        contentProgress.setEnrollment(null);
    }
}
