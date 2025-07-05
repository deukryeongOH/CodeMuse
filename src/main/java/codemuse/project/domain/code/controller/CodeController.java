package codemuse.project.domain.code.controller;

import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.service.CodeService;
import codemuse.project.global.security.spring.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RequestMapping("/code")
@Controller
public class CodeController {
//
//    private final CodeService codeService;
//
//    @PostMapping("/uploadCode")
//    public String upload(@AuthenticationPrincipal CustomUserDetails customUserDetails,
//                         @RequestParam("codeFile") MultipartFile file,
//                         Model model) throws Exception {
//        codeService.saveCodeFile(customUserDetails, file);
//
//        String code = new String(file.getBytes(), StandardCharsets.UTF_8);
//        CodeFeedBackDto feedBackDto = codeService.analyzeAndDevelop(code);
//
//        model.addAttribute("aiFeedback", feedBackDto);
////        model.addAttribute("learningLinks", );
//
//        return "project/manageProjects";
//    }
}
