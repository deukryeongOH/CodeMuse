package codemuse.project.domain.project.dto;

import codemuse.project.domain.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDto {
    private Long Id; // code의 ID
    private String providedCode;
    private Review review;
}
