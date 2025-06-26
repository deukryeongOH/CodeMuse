package codemuse.project.domain.code.entity;

import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;

import java.io.File;
import java.util.Date;
import java.util.List;

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

    private String title;
    private String language;
    private File content_path;
    private Date uploaded_at;
}
