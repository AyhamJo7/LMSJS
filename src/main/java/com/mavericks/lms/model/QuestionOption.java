package com.mavericks.lms.model;

import jakarta.persistence.*;
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
 * Entity representing an option for a multiple-choice question.
 */
@Entity
@Table(name = "question_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "option_text", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Option text is required")
    private String optionText;

    @Column(name = "is_correct")
    private boolean isCorrect = false;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @OneToMany(mappedBy = "selectedOption", cascade = CascadeType.ALL)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Add a student answer to this option.
     *
     * @param studentAnswer the student answer to add
     */
    public void addStudentAnswer(StudentAnswer studentAnswer) {
        studentAnswers.add(studentAnswer);
        studentAnswer.setSelectedOption(this);
    }

    /**
     * Remove a student answer from this option.
     *
     * @param studentAnswer the student answer to remove
     */
    public void removeStudentAnswer(StudentAnswer studentAnswer) {
        studentAnswers.remove(studentAnswer);
        if (studentAnswer.getSelectedOption() == this) {
            studentAnswer.setSelectedOption(null);
        }
    }
}
