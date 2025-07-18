package codemuse.project.domain.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadRequestDto {
    private Long projectId;
    private MultipartFile codeFile;
    private String codeText;
}
