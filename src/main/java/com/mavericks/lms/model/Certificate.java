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
 * Entity representing a certificate issued to a student upon course completion.
 */
@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Size(max = 255, message = "Certificate URL must be less than 255 characters")
    @Column(name = "certificate_url")
    private String certificateUrl;

    @Column(name = "issue_date")
    private LocalDateTime issueDate = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Generate a certificate URL.
     * In a real application, this would trigger a certificate generation process.
     *
     * @return the generated certificate URL
     */
    public String generateCertificateUrl() {
        if (enrollment == null || enrollment.getStudent() == null || enrollment.getCourse() == null) {
            return null;
        }

        // For demonstration purposes, we'll create a simple URL pattern
        // In a real application, this would involve generating a PDF or image file
        String baseUrl = "https://mavericks-lms.com/certificates/";
        String uniqueId = String.format("%d-%d-%d", 
                enrollment.getId(), 
                enrollment.getStudent().getId(), 
                enrollment.getCourse().getId());
        
        this.certificateUrl = baseUrl + uniqueId;
        
        // Update enrollment status
        enrollment.setCertificateIssued(true);
        
        return this.certificateUrl;
    }
}
