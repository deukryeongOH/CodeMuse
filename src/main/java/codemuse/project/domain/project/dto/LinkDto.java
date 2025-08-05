package codemuse.project.domain.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkDto {
    private final String title;
    private final String url;

    public LinkDto(String title, String url) {
        this.title=title;
        this.url=url;
    }
}
