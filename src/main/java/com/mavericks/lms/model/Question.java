package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a question in a quiz or assessment.
 */
@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private CourseContent content;

    @Column(name = "question_text", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Question text is required")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Min(value = 0, message = "Points must be non-negative")
    private Integer points = 1;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Enum representing the different types of questions.
     */
    public enum QuestionType {
        MULTIPLE_CHOICE, TRUE_FALSE, FILL_BLANK, ESSAY
    }

    /**
     * Add an option to this question.
     *
     * @param option the option to add
     */
    public void addOption(QuestionOption option) {
        options.add(option);
        option.setQuestion(this);
    }

    /**
     * Remove an option from this question.
     *
     * @param option the option to remove
     */
    public void removeOption(QuestionOption option) {
        options.remove(option);
        option.setQuestion(null);
    }

    /**
     * Add a student answer to this question.
     *
     * @param studentAnswer the student answer to add
     */
    public void addStudentAnswer(StudentAnswer studentAnswer) {
        studentAnswers.add(studentAnswer);
        studentAnswer.setQuestion(this);
    }

    /**
     * Remove a student answer from this question.
     *
     * @param studentAnswer the student answer to remove
     */
    public void removeStudentAnswer(StudentAnswer studentAnswer) {
        studentAnswers.remove(studentAnswer);
        studentAnswer.setQuestion(null);
    }

    /**
     * Check if this question is multiple choice.
     *
     * @return true if this question is multiple choice, false otherwise
     */
    public boolean isMultipleChoice() {
        return questionType == QuestionType.MULTIPLE_CHOICE;
    }

    /**
     * Check if this question is true/false.
     *
     * @return true if this question is true/false, false otherwise
     */
    public boolean isTrueFalse() {
        return questionType == QuestionType.TRUE_FALSE;
    }

    /**
     * Check if this question is fill in the blank.
     *
     * @return true if this question is fill in the blank, false otherwise
     */
    public boolean isFillBlank() {
        return questionType == QuestionType.FILL_BLANK;
    }

    /**
     * Check if this question is an essay.
     *
     * @return true if this question is an essay, false otherwise
     */
    public boolean isEssay() {
        return questionType == QuestionType.ESSAY;
    }
}
