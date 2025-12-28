package codemuse.project.domain.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String title;
    private String description;

    public ProjectDto(Long id, String title, String description) {
        this.id=id;
        this.title=title;
        this.description = description;
    }
}
