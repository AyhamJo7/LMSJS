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
 * Entity representing a topic in a discussion forum.
 */
@Entity
@Table(name = "discussion_topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id", nullable = false)
    private DiscussionForum forum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Topic title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Topic content is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_pinned")
    private boolean isPinned = false;

    @Column(name = "is_closed")
    private boolean isClosed = false;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscussionComment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Add a comment to this topic.
     *
     * @param comment the comment to add
     */
    public void addComment(DiscussionComment comment) {
        comments.add(comment);
        comment.setTopic(this);
    }

    /**
     * Remove a comment from this topic.
     *
     * @param comment the comment to remove
     */
    public void removeComment(DiscussionComment comment) {
        comments.remove(comment);
        comment.setTopic(null);
    }

    /**
     * Get the number of comments in this topic.
     *
     * @return the number of comments
     */
    @Transient
    public int getCommentCount() {
        return comments.size();
    }

    /**
     * Pin this topic.
     * Pinned topics are displayed at the top of the forum.
     */
    public void pin() {
        this.isPinned = true;
    }

    /**
     * Unpin this topic.
     */
    public void unpin() {
        this.isPinned = false;
    }

    /**
     * Close this topic.
     * Closed topics cannot receive new comments.
     */
    public void close() {
        this.isClosed = true;
    }

    /**
     * Reopen this topic.
     */
    public void reopen() {
        this.isClosed = false;
    }
}
