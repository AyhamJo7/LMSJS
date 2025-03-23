package com.mavericks.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing additional profile information for a user.
 */
@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 255, message = "Profile picture URL must be less than 255 characters")
    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 255, message = "Website URL must be less than 255 characters")
    private String website;

    @Size(max = 50, message = "Twitter handle must be less than 50 characters")
    @Column(name = "twitter_handle")
    private String twitterHandle;

    @Size(max = 255, message = "LinkedIn URL must be less than 255 characters")
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
