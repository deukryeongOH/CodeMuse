package codemuse.project.domain.review.entity;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.example.entity.Example;
import codemuse.project.domain.log.entity.Log;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)
    private Code code;

    @Lob
    @Column(name = "explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation; // 설명

    @Lob
    @Column(name = "improvedCode", nullable = false, columnDefinition = "TEXT")
    private String improvedCode;
    private Date createdAt;

    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Example> examples;

    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Log> logs;

    @Builder
    public Review(String explanation, String improvedCode, Code code){
        this.explanation = explanation;
        this.improvedCode = improvedCode;
        this.code = code;
    }
}
