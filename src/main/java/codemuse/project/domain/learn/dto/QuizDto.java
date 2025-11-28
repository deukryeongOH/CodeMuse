package codemuse.project.domain.learn.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizDto {
    private String question;
    private int answer;
    private List<String> options;
    private String explanation;
}
