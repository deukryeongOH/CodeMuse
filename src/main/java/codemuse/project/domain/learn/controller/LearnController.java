package codemuse.project.domain.learn.controller;

import codemuse.project.domain.learn.dto.QuizDto;
import codemuse.project.domain.learn.service.LearnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/learn")
public class LearnController {

    private final LearnService learnService;

    @GetMapping("/quiz")
    public String goToQuiz(@RequestParam("codeId") Long codeId, Model model){

        List<QuizDto> quizDto = learnService.generateQuiz(codeId);
        model.addAttribute("quizDto", quizDto);

        return "learn/quiz";

    }
}
