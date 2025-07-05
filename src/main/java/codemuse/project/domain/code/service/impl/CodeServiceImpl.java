package codemuse.project.domain.code.service.impl;

//import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.code.repository.CodeRepository;
import codemuse.project.domain.code.service.CodeService;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.project.repository.ProjectRepository;
import codemuse.project.domain.review.entity.Review;
import codemuse.project.domain.review.repository.ReviewRepository;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.repository.UserRepository;
//import codemuse.project.global.ai.AiIntegrationService;
import codemuse.project.global.security.spring.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
//
//    private final ProjectRepository projectRepository;
//    private final CodeRepository codeRepository;
//    private final ReviewRepository reviewRepository;
//    private final AiIntegrationService aiIntegrationService;
//
//    private final Path uploadDir = Paths.get("C:\\Users\\deukr\\CodeMuse\\project\\project\\uploaded-files");
//    private static final String PYTHON_SCRIPT = "C:/Users/deukr/OneDrive/바탕 화면/ai_feedback.py"; // 파이썬 파일 경로
//
//    /**
//     *  업로드 한 코드를 분석해 개선된 코드를 제공함
//     * */
//    public CodeFeedBackDto analyzeAndDevelop(String code) {
//        return null;
//    }
//
//    /**
//     *  코드 업로드(코드 파일 저장 & 코드를 저장할 Project 선택)
//     */
//    @Override
//    public void saveCodeFile(CustomUserDetails customUserDetails, MultipartFile file) {
//        String language = "";
//        String originalFileName = file.getOriginalFilename();
//
//        if(StringUtils.hasText(originalFileName)){
//            int dotIndex = originalFileName.lastIndexOf(".");
//            if(dotIndex >= 0 && dotIndex < originalFileName.length() - 1){
//                language = originalFileName.substring(dotIndex + 1);
//            }
//        }
//        else{
//            throw new IllegalArgumentException("코드 파일을 업로드 해주세요.");
//        }
//
//        if(determineLanguage(language).equals("plain")){
//            throw new IllegalArgumentException("분석 가능한 언어가 아닙니다.");
//        }
//
//        Project project = projectRepository.findByUser(customUserDetails.getUser())
//                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));
//        // 이미 프로젝트 관리 페이지에서 프로젝트 생성된 이후라고 가정(7월 1일)
//        Code code = Code.builder()
//                .language(language)
//                .filePath(originalFileName)
//                .build();
//
//        code.setProject(project);
//        project.getCodes().add(code);
//
//        codeRepository.save(code);
//    }
//
//    private String determineLanguage(String language) {
//        if (language.equals("java")) return "java";
//        if (language.equals("py"))   return "python";
//        if (language.equals("cpp"))   return "c++";
//        if (language.equals("c"))   return "c";
//        if (language.equals("kt"))   return "kotlin";
//        if (language.equals("js"))   return "javascript";
//        if (language.equals("php"))   return "php";
//
//        return "plain";
//    }
}
