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
 * Entity representing content within a course section.
 * Content can be videos, documents, quizzes, or assignments.
 */
@Entity
@Table(name = "course_contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection section;

    @NotBlank(message = "Content title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    @Size(max = 255, message = "Content URL must be less than 255 characters")
    @Column(name = "content_url")
    private String contentUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @NotNull(message = "Position is required")
    @PositiveOrZero(message = "Position must be zero or positive")
    private Integer position;

    @Column(name = "is_free_preview")
    private boolean isFreePreview = false;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentProgress> contentProgresses = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum representing the different types of content.
     */
    public enum ContentType {
        VIDEO, DOCUMENT, QUIZ, ASSIGNMENT
    }

    /**
     * Add a question to this content.
     *
     * @param question the question to add
     */
    public void addQuestion(Question question) {
        questions.add(question);
        question.setContent(this);
    }

    /**
     * Remove a question from this content.
     *
     * @param question the question to remove
     */
    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setContent(null);
    }

    /**
     * Add a content progress record to this content.
     *
     * @param contentProgress the content progress to add
     */
    public void addContentProgress(ContentProgress contentProgress) {
        contentProgresses.add(contentProgress);
        contentProgress.setContent(this);
    }

    /**
     * Remove a content progress record from this content.
     *
     * @param contentProgress the content progress to remove
     */
    public void removeContentProgress(ContentProgress contentProgress) {
        contentProgresses.remove(contentProgress);
        contentProgress.setContent(null);
    }
}
