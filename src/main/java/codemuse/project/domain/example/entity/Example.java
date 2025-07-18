package codemuse.project.domain.example.entity;

import codemuse.project.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "example")
public class Example { // 업로드한 코드와 유사한 코드

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    private String title;
    private String language;
    private String codeSnippet; // 예시(유사) 코드
    private String explanation; // 예시(유사) 코드에 대한 설명
}
