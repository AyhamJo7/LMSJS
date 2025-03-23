package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a student's review of a course.
 */
@Entity
@Table(name = "course_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "is_approved")
    private boolean isApproved = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Get the course being reviewed.
     *
     * @return the course being reviewed
     */
    @Transient
    public Course getCourse() {
        return enrollment != null ? enrollment.getCourse() : null;
    }

    /**
     * Get the student who wrote the review.
     *
     * @return the student who wrote the review
     */
    @Transient
    public User getStudent() {
        return enrollment != null ? enrollment.getStudent() : null;
    }

    /**
     * Submit the review.
     * This method updates the course's average rating.
     */
    public void submitReview() {
        if (enrollment != null && enrollment.getCourse() != null) {
            enrollment.getCourse().calculateAverageRating();
        }
    }

    /**
     * Approve the review.
     * Reviews may be moderated before being displayed publicly.
     */
    public void approve() {
        this.isApproved = true;
    }

    /**
     * Reject the review.
     * Reviews may be moderated before being displayed publicly.
     */
    public void reject() {
        this.isApproved = false;
    }
}
