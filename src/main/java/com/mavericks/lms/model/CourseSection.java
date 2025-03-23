package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a section within a course.
 * Sections can contain multiple content items like videos, documents, quizzes, etc.
 */
@Entity
@Table(name = "course_sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Section title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Position is required")
    @PositiveOrZero(message = "Position must be zero or positive")
    private Integer position;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseContent> contents = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Add content to this section.
     *
     * @param content the content to add
     */
    public void addContent(CourseContent content) {
        contents.add(content);
        content.setSection(this);
    }

    /**
     * Remove content from this section.
     *
     * @param content the content to remove
     */
    public void removeContent(CourseContent content) {
        contents.remove(content);
        content.setSection(null);
    }
}
