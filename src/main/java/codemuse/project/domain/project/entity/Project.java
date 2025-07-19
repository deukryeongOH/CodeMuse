package codemuse.project.domain.project.entity;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Code> codes;

    private String title;
    private String description; // 상세정보 들어가면 출력
    private LocalDateTime createdAt;

    @Builder
    public Project(String title, String description){
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

}
