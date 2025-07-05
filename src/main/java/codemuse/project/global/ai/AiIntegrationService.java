package codemuse.project.global.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

//@Service
//public class AiIntegrationService {
//    /**
//     * 외부 Python 스크립트(ai_feedback.py)를 호출해서
//     * JSON 형식의 피드백 결과를 받아 파싱
//     */
//    public AiFeedBackResultDto getFeedback(File codeFile) throws Exception {
//        ProcessBuilder pb = new ProcessBuilder(
//                "python3", "scripts/ai_feedback.py", codeFile.getAbsolutePath()
//        );
//        pb.redirectErrorStream(true);
//        var process = pb.start();
//        // 최대 60초 대기
//        if (!process.waitFor(60, TimeUnit.SECONDS)) {
//            process.destroyForcibly();
//            throw new RuntimeException("AI 피드백 스크립트 응답 지연");
//        }
//        try (InputStream is = process.getInputStream()) {
//            return new ObjectMapper()
//                    .readValue(is, AiFeedBackResultDto.class);
//        }
//    }
//}
