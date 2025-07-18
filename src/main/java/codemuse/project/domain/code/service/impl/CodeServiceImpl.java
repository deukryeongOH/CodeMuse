package codemuse.project.domain.code.service.impl;

//import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.code.repository.CodeRepository;
import codemuse.project.domain.code.service.CodeService;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.project.repository.ProjectRepository;
import codemuse.project.domain.review.entity.Review;
import codemuse.project.domain.review.repository.ReviewRepository;
import codemuse.project.global.ai.ChatGPTService;
import codemuse.project.global.ai.QuotaExceededException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.Paths;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {

    private final ProjectRepository projectRepository;
    private final CodeRepository codeRepository;
    private final ReviewRepository reviewRepository;
    private final ChatGPTService chatGPTService;

    private final Path uploadDir = Paths.get("C:\\Users\\deukr\\CodeMuse\\project\\project\\uploaded-files");
    private static final String PYTHON_SCRIPT = "C:/Users/deukr/OneDrive/바탕 화면/ai_feedback.py"; // 파이썬 파일 경로

    /**
     *  업로드 한 코드를 분석해 개선된 코드를 제공함
     * */
    public CodeFeedBackDto analyzeAndDevelop(Long codeId) {
        Code code = codeRepository.findById(codeId)
                .orElseThrow(() -> new IllegalArgumentException("코드 정보가 존재하지 않습니다."));

        String prompt = generatePrompt(code.getCode());

        CodeFeedBackDto dto;
        try {
            dto = chatGPTService.chatCompletion(prompt);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("////////SERVER_ERROR////////");
        } catch (QuotaExceededException e) {
            throw new RuntimeException(e);
        }

        Review review = Review.builder()
                .code(code)
                .improvedCode(dto.getImprovedCode())
                .explanation(dto.getExplanation())
                .build();

        reviewRepository.save(review);

        return dto;
    }

    private String generatePrompt(String code) {
        StringBuilder sb = new StringBuilder();

        sb.append(code).append("\n");

        sb.append("너는 오직 JSON 형태로만 응답해야 해.")
                .append("문자열 내부의 개행(new line, code 10)은 반드시 \\\\n으로 이스케이프 처리해야 해.").append("\n")
                .append("응답 JSON은 pretty‑print 형식으로, 그리고 improvedCode 필드는 Markdown Java 코드블록으로 표현:\n")
                .append("explanation은 한국어로 표현").append("\n")
                .append("반환 형식은 아래와 같아:").append("\n")
                .append("{").append("\n")
                .append("  \"improvedCode\": \"<개선된 코드 전체 문자열>\",").append("\n")
                .append("  \"explanation\": \"<코드 개선에 대한 설명>\"").append("\n")
                .append("}").append("\n");

        // 실제 요청
        sb.append("너는 컴퓨터공학 경력 10년차 개발자야. ").append("\n")
                .append("위 코드를 다음 기준에 따라 개선하고, ").append("\n")
                .append("반드시 위 JSON 스키마에 맞춰서만 응답해줘: ").append("\n")
                .append("- Deprecated API는 모두 대체 가능한 최신 방식으로 변경하고\n")
                .append("- 실무에 바로 적용 가능하도록 오류 없이 작성해").append("\n")
                .append("- 시간 복잡도와 공간 복잡도를 모두 고려해 최적화해서 결과를 도출해줘").append("\n");

        return sb.toString();
    }

    /**
     *  코드 업로드(코드를 저장할 Project 선택 후 코드 저장)
     */
    @Override
    public Long saveCode(Long projectId, MultipartFile file) throws IOException {
        String code = new String(file.getBytes(), StandardCharsets.UTF_8);

        Code savedCode = Code.builder()
                .code(code)
                .build();

        Project findProject = projectRepository.findById(projectId)
                        .orElseThrow();

        savedCode.setProject(findProject);
        findProject.getCodes().add(savedCode);
        codeRepository.save(savedCode);

        return savedCode.getId();
    }
}
