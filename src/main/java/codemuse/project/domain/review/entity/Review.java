package codemuse.project.domain.review.entity;

import codemuse.project.domain.code.dto.LearningLink;
import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.link.entity.Link;
import codemuse.project.domain.learn.entity.Learn;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "review")
    private Code code;

    @Lob
    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation; // 설명

    @Lob
    @Column(name = "improvedCode", columnDefinition = "TEXT")
    private String improvedCode; // 개선된 코드
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Link> links;

    @Builder
    public Review(String explanation, String improvedCode) {
        this.explanation = explanation;
        this.improvedCode = improvedCode;
        this.createdAt = LocalDateTime.now();
    }
}
