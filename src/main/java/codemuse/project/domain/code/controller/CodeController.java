package codemuse.project.domain.code.controller;

//import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.dto.LearningLink;
import codemuse.project.domain.code.dto.UploadRequestDto;
import codemuse.project.domain.code.service.CodeService;
import codemuse.project.domain.project.repository.ProjectRepository;
import codemuse.project.domain.user.entity.User;
import codemuse.project.global.security.spring.CustomUserDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/code")
@Controller
public class CodeController {

    private final CodeService codeService;

    @PostMapping("/uploadCode")
    public String upload(@ModelAttribute("uploadRequestDto") UploadRequestDto dto,
                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                         Model model) throws Exception {
        User user = customUserDetails.getUser();
        Long codeId = codeService.checkInput(dto);

        if (codeId == null) {
            model.addAttribute("errorMessage", "파일 또는 코드 텍스트를 반드시 입력해주세요.");
            return "dashboard";
        }


        CodeFeedBackDto feedBackDto = codeService.analyzeAndDevelop(codeId);
        model.addAttribute("aiFeedback", feedBackDto);
        model.addAttribute("projects", user.getProjects());

        return "dashboard";
    }
}
