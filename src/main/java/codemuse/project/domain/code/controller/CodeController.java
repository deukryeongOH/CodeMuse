package codemuse.project.domain.code.controller;

import codemuse.project.domain.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/code")
@Controller
public class CodeController {

    private final CodeService codeService;
}
