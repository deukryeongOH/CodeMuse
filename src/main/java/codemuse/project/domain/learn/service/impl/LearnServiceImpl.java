package codemuse.project.domain.learn.service.impl;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.code.repository.CodeRepository;
import codemuse.project.domain.learn.dto.QuizDto;
import codemuse.project.domain.learn.service.LearnService;
import codemuse.project.domain.review.entity.Review;
import codemuse.project.global.ai.ChatGPTService;
import codemuse.project.global.ai.QuotaExceededException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class LearnServiceImpl implements LearnService {

    private final CodeRepository codeRepository;
    private final ChatGPTService chatGPTService;

    @Override
    public List<QuizDto> generateQuiz(Long codeId) {
        Code findCode = codeRepository.findById(codeId)
                .orElseThrow(IllegalArgumentException::new);

        Review findReview = findCode.getReview();

        String originCode = findCode.getProvidedCode();
        String improvedCode = findReview.getImprovedCode();

        String prompt = providePrompt(originCode, improvedCode);

        List<QuizDto> quizDto;
        try {
            quizDto = chatGPTService.generatedQuiz(prompt);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("////////SERVER_ERROR////////");
        } catch (QuotaExceededException e) {
            throw new RuntimeException(e);
        }

        return quizDto;
    }

    private String providePrompt(String originCode, String improvedCode) {
        return """
            당신은 시니어 자바 백엔드 개발자이자 IT 기술 면접관입니다.
            아래 제공되는 [기존 코드]와 [개선된 코드]를 비교 분석하여, 학습자가 실력을 키울 수 있는 **객관식 퀴즈 5개**를 생성해주세요.
            
            ### 출제 원칙
            1. 단순 문법보다는 **개선된 이유(성능 최적화, 보안 강화, 디자인 패턴 적용, 가독성, 유지보수성 등)**에 초점을 맞추세요.
            2. 보기는 4개(4지선다형)로 구성하세요.
            3. 정답은 1부터 4 사이의 정수여야 합니다.
            4. 해설은 초보자도 이해하기 쉽게 구체적이고 친절하게 작성해주세요 (한국어).
            5. **중요:** 응답은 Markdown 태그(```json 등)나 서두/결말 없이 **오직 순수 JSON 데이터만** 반환해야 합니다.
            
            ### JSON 반환 포맷 예시
            [
              {
                "question": "문제 내용...",
                "options": ["보기1", "보기2", "보기3", "보기4"],
                "answer": 1,
                "explanation": "해설 내용..."
              }
            ]
            
            ---
            
            [기존 코드]
            %s
            
            [개선된 코드]
            %s
            """.formatted(originCode, improvedCode);

    }
}
