package codemuse.project.domain.learn.service;

import codemuse.project.domain.learn.dto.QuizDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface LearnService {
    List<QuizDto> generateQuiz(Long codeId);
}
