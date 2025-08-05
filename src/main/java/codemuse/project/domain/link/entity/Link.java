package codemuse.project.domain.link.entity;

import codemuse.project.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "link")
public class Link { // AI 피드백을 통해 도출된 link

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private String title;
    private String url;

    @Builder
    public Link(String title, String url){
        this.title = title;
        this.url = url;
    }
}
