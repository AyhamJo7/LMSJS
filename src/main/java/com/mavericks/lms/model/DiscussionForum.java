package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
 * Entity representing a discussion forum for a course.
 */
@Entity
@Table(name = "discussion_forums")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionForum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Forum title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscussionTopic> topics = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Add a topic to this forum.
     *
     * @param topic the topic to add
     */
    public void addTopic(DiscussionTopic topic) {
        topics.add(topic);
        topic.setForum(this);
    }

    /**
     * Remove a topic from this forum.
     *
     * @param topic the topic to remove
     */
    public void removeTopic(DiscussionTopic topic) {
        topics.remove(topic);
        topic.setForum(null);
    }

    /**
     * Get the number of topics in this forum.
     *
     * @return the number of topics
     */
    @Transient
    public int getTopicCount() {
        return topics.size();
    }

    /**
     * Get the total number of comments in this forum.
     *
     * @return the total number of comments
     */
    @Transient
    public int getTotalCommentCount() {
        return topics.stream()
                .mapToInt(DiscussionTopic::getCommentCount)
                .sum();
    }
}
