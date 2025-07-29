package codemuse.project.domain.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeFeedBackDto {


    private Long reviewId;

    @JsonProperty("improvedCode")
    private String improvedCode;

    @JsonProperty("explanation")
    private String explanation;

    @JsonProperty("links")
    private List<LearningLink> links;
}
