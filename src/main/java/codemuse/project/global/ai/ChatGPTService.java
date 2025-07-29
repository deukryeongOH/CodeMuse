package codemuse.project.global.ai;

import codemuse.project.domain.code.dto.CodeFeedBackDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTService {

    @Value("${openai.secret-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public CodeFeedBackDto chatCompletion(String prompt) throws JsonProcessingException, QuotaExceededException {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(message)
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // 3) API 호출 시 ChatCompletionResponse로 바로 매핑
        try {
            ResponseEntity<String> resp = restTemplate
                    .exchange(url, HttpMethod.POST, entity, String.class);

            ObjectMapper mapper = new ObjectMapper()
                    .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
                    .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);

            //Test
            String rawBody = Objects.requireNonNull(resp.getBody()).toString();

            JsonNode wrapper = mapper.readTree(rawBody);
            JsonNode contentNode = wrapper
                    .path("choices").get(0)
                    .path("message").path("content");
            String content = contentNode.asText().trim();

            return mapper.readValue(content, CodeFeedBackDto.class);

//            // 1) AI 출력만 꺼내기 (기존 그대로)
//            String content = Objects.requireNonNull(resp.getBody())
//                    .getChoices().getFirst()
//                    .getMessage().getContent().trim();
//            log.debug("원본 AI 응답:\n{}", content);
//
//            // 2) ```json …``` 코드펜스 안의 JSON만 추출
//            Pattern fence = Pattern.compile("```json\\s*([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
//            Matcher m = fence.matcher(content);
//            if (m.find()) {
//                content = m.group(1).trim();
//            }
//            log.debug("펜스 제거 후 content:\n{}", content);
//
//            // 3) 배열 래퍼 처리 및 구조 검사
//            ObjectMapper mapper = new ObjectMapper()
//                    .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
//                    .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
//
//            JsonNode root = mapper.readTree(content);
//            log.debug("readTree 후 JsonNode 타입: {}", root.getNodeType());
//
//            // 배열이면 첫 번째 원소만
//            if (root.isArray()) {
//                if (root.isEmpty()) {
//                    throw new RuntimeException("AI가 빈 배열을 반환했습니다.");
//                }
//                root = root.get(0);
//                log.debug("배열 언랩 후 JsonNode 타입: {}", root.getNodeType());
//            }
//            // 객체가 아니면 에러
//            if (!root.isObject()) {
//                throw new RuntimeException("예상치 못한 JSON 구조: " + root.getNodeType());
//            }
//            log.debug("▶▶▶ GPT raw content:\n{}", content);
//
//            // 4) DTO로 변환 (기존 그대로)
//            return mapper.treeToValue(root, CodeFeedBackDto.class);






//            // 1) AI 출력만 꺼내기
//            String content = Objects.requireNonNull(resp.getBody())
//                    .getChoices().getFirst()
//                    .getMessage().getContent().trim();
//
//            // 2) ```json …``` 코드펜스 제거
//            if (content.startsWith("```")) {
//                int start = content.indexOf('\n') + 1;
//                int end   = content.lastIndexOf("```");
//                content = content.substring(start, end).trim();
//            }
//
//            // 3) 배열 래퍼 제거
//            ObjectMapper mapper = new ObjectMapper()
//                    .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
//                    .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
//
//            JsonNode root = mapper.readTree(content);
//            if (root.isArray()) {
//                if (root.isEmpty()) {
//                    throw new RuntimeException("AI가 빈 배열을 반환했습니다.");
//                }
//                root = root.get(0);
//            }
//
//            // 4) DTO로 변환
//            CodeFeedBackDto dto = mapper.treeToValue(root, CodeFeedBackDto.class);
//            return dto;
        }
        catch (HttpClientErrorException.TooManyRequests ex) {
            throw new QuotaExceededException("OpenAI 할당량 초과: 크레딧을 확인하세요.");
        }
//        catch (JsonProcessingException e) {
//            // 파싱 직전의 JSON을 같이 남겨서 문제를 더 쉽게 디버깅
//            throw new RuntimeException("AI 응답 파싱 오류:\n" + e.getMessage()
//                    + "\n--- JSON ---\n" + jsonOnly, e);
//        }
    }

    private static String getJsonOnly(ResponseEntity<ChatCompletionResponse> resp) {
        String content = Objects.requireNonNull(resp.getBody())
                .getChoices().getFirst()
                .getMessage().getContent()
                .trim();
        // 4) 모델이 반환한 content를 JSON으로 파싱하여 AiFeedBackResultDto에 매핑
        int start = content.indexOf("{");
        int end   = content.lastIndexOf("}") + 1;
        if (start < 0 || end <= start) {
            throw new RuntimeException("AI 응답에서 JSON 객체를 찾을 수 없습니다:\n" + content);
        }
        return content.substring(start, end);
    }
}
