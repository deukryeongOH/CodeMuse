package codemuse.project.global.ai;

import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.code.repository.CodeRepository;
import codemuse.project.domain.learn.dto.QuizDto;
import codemuse.project.domain.review.entity.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
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

    private final ObjectMapper objectMapper;
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
        }
        catch (HttpClientErrorException.TooManyRequests ex) {
            throw new QuotaExceededException("OpenAI 할당량 초과: 크레딧을 확인하세요.");
        }
    }

    public List<QuizDto> generatedQuiz(String prompt) throws JsonProcessingException, QuotaExceededException{
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
                "messages", List.of(message),
                "temperature", 0.2
        );
        String jsonBody = objectMapper.writeValueAsString(body);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        try {
            ResponseEntity<String> resp = restTemplate
                    .exchange(url, HttpMethod.POST, entity, String.class);

            String rawBody = Objects.requireNonNull(resp.getBody());

            JsonNode wrapper = objectMapper.readTree(rawBody);
            String content = wrapper
                    .path("choices").get(0)
                    .path("message").path("content")
                    .asText();

            content = content.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            return objectMapper.readValue(content, new TypeReference<List<QuizDto>>() {});
        }
        catch (HttpClientErrorException.BadRequest ex) {
            throw new RuntimeException("AI 요청 형식이 잘못되었습니다.");
        }
        catch (HttpClientErrorException.TooManyRequests | JsonProcessingException ex) {
            throw new QuotaExceededException("OpenAI Quota Exceeded");
        }
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
