package codemuse.project.domain.code.controller;

//import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.dto.CodeFeedBackDto;
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

@RequiredArgsConstructor
@RequestMapping("/code")
@Controller
public class CodeController {

    private final CodeService codeService;
    private final ProjectRepository projectRepository;

    @PostMapping("/uploadCode")
    public String upload(@ModelAttribute("uploadRequestDto") UploadRequestDto dto,
                         @RequestParam("codeFile") MultipartFile file,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                         Model model) throws Exception {
        User user = customUserDetails.getUser();
        Long codeId = codeService.saveCode(dto.getProjectId(), file);

        CodeFeedBackDto feedBackDto = codeService.analyzeAndDevelop(codeId);
        model.addAttribute("aiFeedback", feedBackDto);
        model.addAttribute("projects", user.getProjects());
        //model.addAttribute("learningLinks", );

        return "dashboard";
    }
}
