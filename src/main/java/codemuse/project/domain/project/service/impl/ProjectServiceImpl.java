package codemuse.project.domain.project.service.impl;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.code.repository.CodeRepository;
import codemuse.project.domain.link.entity.Link;
import codemuse.project.domain.project.dto.CodeDto;
import codemuse.project.domain.project.dto.CreateProjectDto;
import codemuse.project.domain.project.dto.LinkDto;
import codemuse.project.domain.project.dto.ProjectDto;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.project.repository.ProjectRepository;
import codemuse.project.domain.project.service.ProjectService;
import codemuse.project.domain.review.repository.ReviewRepository;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.repository.UserRepository;
import codemuse.project.global.security.spring.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final ObjectMapper objectMapper;
    /**
     * 프로젝트 리스트 반환
     */
    @Override
    public List<ProjectDto> findAllProject(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<ProjectDto> list = new ArrayList<>();

        for (Project p : user.getProjects()) {
            ProjectDto dto = new ProjectDto();
            dto.setId(p.getId());
            dto.setTitle(p.getTitle());
            dto.setDescription(p.getDescription());
            list.add(dto);
        }

        return list;
    }

    /**
     * 프로젝트 생성
     */
    @Override
    public void generateProject(CreateProjectDto dto,
                                   CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        Project project = Project.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();

        project.setUser(user);
        user.getProjects().add(project);

        userRepository.save(user);
//        projectRepository.save(project);
//        유저만 저장하면 Cascade 설정으로 인해 프로젝트도 함께 저장됨.
    }

    @Override
    public void deleteProject(Long projectId, CustomUserDetails customUserDetails) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트 정보가 없습니다."));
        User user = customUserDetails.getUser();

        user.getProjects().remove(project);
        project.setUser(null);
        projectRepository.delete(project);
    }

    @Override
    public List<CodeDto> getCodeList(CustomUserDetails customUserDetails,
                                             Long projectId) {
        User user = customUserDetails.getUser();
        List<CodeDto> codes = new ArrayList<>();

        for(Project p : user.getProjects()){
            if(Objects.equals(p.getId(), projectId)){
                for(Code c : p.getCodes()){
                    CodeDto dto = new CodeDto();
                    dto.setProvidedCode(c.getProvidedCode());
                    dto.setReview(c.getReview());
                    dto.setId(c.getId());
                    codes.add(dto);
                }
            }
        }

        return codes;
    }

    @Override
    public ProjectDto getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트가 없습니다."));

        ProjectDto dto = new ProjectDto();
        dto.setDescription(project.getDescription());
        dto.setTitle(project.getTitle());
        dto.setId(project.getId());

        return dto;
    }

    @Override
    public void deleteCodeFromProject(Long projectId, Long codeId) {
        Project findProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 유효하지 않습니다."));

        Code code = codeRepository.findById(codeId)
                .orElseThrow(() -> new IllegalArgumentException("코드가 존재하지 않습니다."));

        findProject.getCodes().remove(code);
        codeRepository.delete(code);
    }

    @Override
    public String EncodeLink(List<CodeDto> codes) throws JsonProcessingException {

        List<LinkDto> links = new ArrayList<>();

        for (CodeDto dto : codes) {
            if (dto.getReview() != null && dto.getReview().getLinks() != null) {
                for (Link l : dto.getReview().getLinks()) {
                    LinkDto linkDto = new LinkDto(l.getTitle(), l.getUrl());
                    links.add(linkDto);
                }
            }else{
                throw new IllegalArgumentException("Review 객체가 null입니다.");
            }
        }

        return objectMapper.writeValueAsString(links);
    }
}
