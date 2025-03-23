package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a user in the system.
 * Users can have different roles (Student, Instructor, Admin).
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false)
    private String password;

    @Size(max = 50, message = "State must be less than 50 characters")
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> instructorCourses = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum representing the different roles a user can have.
     */
    public enum Role {
        STUDENT, INSTRUCTOR, ADMIN
    }

    /**
     * Add a course to the instructor's list of courses.
     *
     * @param course the course to add
     */
    public void addCourse(Course course) {
        instructorCourses.add(course);
        course.setInstructor(this);
    }

    /**
     * Remove a course from the instructor's list of courses.
     *
     * @param course the course to remove
     */
    public void removeCourse(Course course) {
        instructorCourses.remove(course);
        course.setInstructor(null);
    }

    /**
     * Add an enrollment to the student's list of enrollments.
     *
     * @param enrollment the enrollment to add
     */
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        if (enrollment.getStudent() != this) {
            enrollment.setStudent(this);
        }
    }

    /**
     * Remove an enrollment from the student's list of enrollments.
     *
     * @param enrollment the enrollment to remove
     */
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        if (enrollment.getStudent() == this) {
            enrollment.setStudent(null);
        }
    }
}
