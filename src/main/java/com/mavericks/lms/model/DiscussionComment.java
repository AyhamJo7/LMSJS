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
 * Entity representing a comment in a discussion topic.
 */
@Entity
@Table(name = "discussion_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private DiscussionTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Comment content is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private DiscussionComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscussionComment> replies = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Add a reply to this comment.
     *
     * @param reply the reply to add
     */
    public void addReply(DiscussionComment reply) {
        replies.add(reply);
        reply.setParent(this);
    }

    /**
     * Remove a reply from this comment.
     *
     * @param reply the reply to remove
     */
    public void removeReply(DiscussionComment reply) {
        replies.remove(reply);
        reply.setParent(null);
    }

    /**
     * Get the number of replies to this comment.
     *
     * @return the number of replies
     */
    @Transient
    public int getReplyCount() {
        return replies.size();
    }

    /**
     * Check if this comment is a reply.
     *
     * @return true if this comment is a reply, false otherwise
     */
    @Transient
    public boolean isReply() {
        return parent != null;
    }

    /**
     * Check if this comment has replies.
     *
     * @return true if this comment has replies, false otherwise
     */
    @Transient
    public boolean hasReplies() {
        return !replies.isEmpty();
    }
}
