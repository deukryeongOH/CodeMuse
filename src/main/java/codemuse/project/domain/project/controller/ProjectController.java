package codemuse.project.domain.project.controller;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.project.dto.CreateProjectDto;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.project.service.ProjectService;
import codemuse.project.global.security.spring.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    /**
     *  프로젝트 리스트 컨트롤러
     */
    @GetMapping("/manageProjects")
    public String projectList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              Model model){
        List<Project> list = projectService.findAllProject(customUserDetails);
        model.addAttribute("projects", list);
        return "project/manageProjects";
    }

    /**
     * 프로젝트 생성 컨트롤러
     * */

    @GetMapping("/createProject")
    public String create(Model model){
        model.addAttribute("createProjectDto", new CreateProjectDto());
        return "project/createProject";
    }

    @PostMapping("/createProject")
    public String create(@ModelAttribute("createProjectDto") CreateProjectDto dto,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        projectService.generateProject(dto, customUserDetails);
        return "redirect:/project/manageProjects";
    }

    @PostMapping("/deleteProject/{id}")
    public String deleteProject(@PathVariable("id") Long projectId,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        projectService.deleteProject(projectId, customUserDetails);
        return "redirect:/project/manageProjects"; // 삭제 후 프로젝트 목록 페이지로 리다이렉트
    }

    @GetMapping("/details/{id}")
    public String projectDetails(@PathVariable("id") Long projectId,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 Model model){
        List<Code> codes = projectService.getCodeList(customUserDetails, projectId);
        Project project = projectService.getProject(projectId);

        model.addAttribute("codes", codes);
        model.addAttribute("projectId", projectId);
        model.addAttribute("title", project.getTitle());
        model.addAttribute("description", project.getDescription());
        return "project/details";
    }

    @PostMapping("{projectId}/delete/code/{codeId}")
    public String deleteCodeFromProject(@PathVariable("projectId") Long projectId,
                                        @PathVariable("codeId") Long codeId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        Model model){
        projectService.deleteCodeFromProject(projectId, codeId);

        Project project = projectService.getProject(projectId);
        List<Code> codes = projectService.getCodeList(customUserDetails, projectId);

        model.addAttribute("codes", codes);
        model.addAttribute("projectId", projectId);
        model.addAttribute("title", project.getTitle());
        model.addAttribute("description", project.getDescription());

        return "redirect:/project/details/" + projectId;
    }

}
