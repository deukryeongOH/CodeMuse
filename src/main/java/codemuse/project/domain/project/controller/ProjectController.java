package codemuse.project.domain.project.controller;

import codemuse.project.domain.code.dto.LearningLink;
import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.link.entity.Link;
import codemuse.project.domain.project.dto.CodeDto;
import codemuse.project.domain.project.dto.CreateProjectDto;
import codemuse.project.domain.project.dto.ProjectDto;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.project.service.ProjectService;
import codemuse.project.domain.review.entity.Review;
import codemuse.project.domain.review.service.ReviewService;
import codemuse.project.global.security.spring.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ReviewService reviewService;

    /**
     *  프로젝트 리스트 컨트롤러
     */
    @GetMapping("/manageProjects")
    public String projectList(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              Model model){
        List<ProjectDto> list = projectService.findAllProject(customUserDetails);
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
                                 Model model) throws JsonProcessingException {
        List<CodeDto> codes = projectService.getCodeList(customUserDetails, projectId);
        ProjectDto projectDto = projectService.getProject(projectId);

        String linkJson = projectService.EncodeLink(codes);

        model.addAttribute("codes", codes);
        model.addAttribute("projectId", projectDto.getId());
        model.addAttribute("title", projectDto.getTitle());
        model.addAttribute("description", projectDto.getDescription());
        model.addAttribute("linksJson", linkJson);
        return "project/details";
    }

    @PostMapping("{projectId}/delete/code/{codeId}")
    public String deleteCodeFromProject(@PathVariable("projectId") Long projectId,
                                        @PathVariable("codeId") Long codeId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        Model model){
        projectService.deleteCodeFromProject(projectId, codeId);

        ProjectDto projectDto = projectService.getProject(projectId);
        List<CodeDto> codes = projectService.getCodeList(customUserDetails, projectId);

        model.addAttribute("codes", codes);
        model.addAttribute("projectId", projectDto.getId());
        model.addAttribute("title", projectDto.getTitle());
        model.addAttribute("description", projectDto.getDescription());

        return "redirect:/project/details/" + projectId;
    }

}
