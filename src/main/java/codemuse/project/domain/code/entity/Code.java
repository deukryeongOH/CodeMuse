package codemuse.project.domain.code.entity;

import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.time.LocalTime.now;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "code")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(
            mappedBy = "code",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Review> reviews;

    private String language;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String providedCode;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String improvedCode;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String explanation;

    private LocalDateTime uploaded_at;

    @Builder
    public Code(String language, String providedCode){
        this.language = language;
        this.providedCode = providedCode;
        this.uploaded_at = LocalDateTime.now();
    }
}
