package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
 * Entity representing a course in the Learning Management System.
 */
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CourseCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    private boolean published = false;

    private boolean featured = false;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseSection> sections = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DiscussionForum> discussionForums = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Announcement> announcements = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum representing the difficulty level of a course.
     */
    public enum Level {
        BEGINNER, INTERMEDIATE, ADVANCED, ALL_LEVELS
    }

    /**
     * Add a section to the course.
     *
     * @param section the section to add
     */
    public void addSection(CourseSection section) {
        sections.add(section);
        section.setCourse(this);
    }

    /**
     * Remove a section from the course.
     *
     * @param section the section to remove
     */
    public void removeSection(CourseSection section) {
        sections.remove(section);
        section.setCourse(null);
    }

    /**
     * Add an enrollment to the course.
     *
     * @param enrollment the enrollment to add
     */
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }

    /**
     * Remove an enrollment from the course.
     *
     * @param enrollment the enrollment to remove
     */
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }

    /**
     * Calculate the average rating based on course reviews.
     */
    public void calculateAverageRating() {
        if (enrollments.isEmpty()) {
            this.averageRating = null;
            return;
        }

        // Implementation will be added when CourseReview entity is created
    }
}
