package codemuse.project.domain.user.entity;

import codemuse.project.global.role.Role;
import codemuse.project.domain.learn.entity.Learn;
import codemuse.project.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users") // MySQL 예약어 충돌 방지
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 사용자 명

    @Column(name = "accountId", unique = true, nullable = false)
    private String accountId; // 사용자 아이디

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Project> projects;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Learn> learns;


    @Builder
    public User(String name, String accountId, String password, String email, String nickName, LocalDateTime createdAt){
        this.name = name;
        this.accountId = accountId;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.createdAt = createdAt;
    }

}
