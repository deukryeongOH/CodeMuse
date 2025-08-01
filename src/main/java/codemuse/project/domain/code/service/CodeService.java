package codemuse.project.domain.code.service;

//import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.dto.LearningLink;
import codemuse.project.domain.code.dto.UploadRequestDto;
import codemuse.project.global.security.spring.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public interface CodeService {
    CodeFeedBackDto analyzeAndDevelop(Long codeId);
    Long saveCode(Long projectId, String code) throws IOException;
    Long checkInput(UploadRequestDto dto) throws IOException;
}