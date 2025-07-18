package codemuse.project.domain.project.dto;

import codemuse.project.domain.code.entity.Code;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectCodesDto {
    private List<Code> codes;
}
