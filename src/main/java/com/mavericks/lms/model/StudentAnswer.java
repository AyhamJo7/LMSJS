package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a student's answer to a question.
 */
@Entity
@Table(name = "student_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private QuestionOption selectedOption;

    @Column(name = "essay_answer", columnDefinition = "TEXT")
    private String essayAnswer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Min(value = 0, message = "Points earned must be non-negative")
    @Column(name = "points_earned")
    private Integer pointsEarned = 0;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Check if the answer is correct for an automatically graded question.
     * This method sets the isCorrect field and calculates the points earned.
     */
    public void checkAnswer() {
        if (question.isMultipleChoice() || question.isTrueFalse()) {
            if (selectedOption != null) {
                this.isCorrect = selectedOption.isCorrect();
                this.pointsEarned = this.isCorrect ? question.getPoints() : 0;
            } else {
                this.isCorrect = false;
                this.pointsEarned = 0;
            }
        } else if (question.isFillBlank()) {
            // For fill-in-the-blank questions, a simple implementation might check for exact match
            // A more sophisticated implementation would use pattern matching or other criteria
            boolean matchFound = false;
            for (QuestionOption option : question.getOptions()) {
                if (option.isCorrect() && option.getOptionText().trim().equalsIgnoreCase(essayAnswer.trim())) {
                    matchFound = true;
                    break;
                }
            }
            this.isCorrect = matchFound;
            this.pointsEarned = this.isCorrect ? question.getPoints() : 0;
        }
        // Essay questions require manual grading, so isCorrect and pointsEarned are set by the instructor
    }

    /**
     * Grade an essay answer manually.
     *
     * @param isCorrect whether the answer is correct
     * @param pointsEarned the points earned
     */
    public void gradeEssay(boolean isCorrect, int pointsEarned) {
        if (question.isEssay()) {
            this.isCorrect = isCorrect;
            this.pointsEarned = Math.min(pointsEarned, question.getPoints());
        }
    }
}
