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
                         @RequestParam(value = "codeFile", required = false) MultipartFile file,
                         @RequestParam(value = "codeText", required = false) String textCode,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                         Model model) throws Exception {
        User user = customUserDetails.getUser();
        Long codeId;
        if (textCode == null && !file.isEmpty()) {
            String code = new String(file.getBytes(), StandardCharsets.UTF_8);
            codeId = codeService.saveCode(dto.getProjectId(), code);
        } else if (textCode != null && file.isEmpty()) {
            codeId = codeService.saveCode(dto.getProjectId(), textCode);
        }
        else{
            model.addAttribute("errorMessage", "파일 또는 코드 텍스트를 반드시 입력해주세요.");
            return "dashboard";
        }

        CodeFeedBackDto feedBackDto = codeService.analyzeAndDevelop(codeId);
        model.addAttribute("aiFeedback", feedBackDto);
        model.addAttribute("projects", user.getProjects());

        return "dashboard";
    }
}
