package codemuse.project.domain.project.service.impl;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.project.dto.CreateProjectDto;
import codemuse.project.domain.project.dto.ProjectCodesDto;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.project.repository.ProjectRepository;
import codemuse.project.domain.project.service.ProjectService;
import codemuse.project.domain.user.dto.UserResetPwdDto;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.repository.UserRepository;
import codemuse.project.global.security.spring.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * 프로젝트 리스트 반환
     */
    @Override
    public List<Project> findAllProject(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        return new ArrayList<>(user.getProjects());
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

        projectRepository.delete(project);
        user.getProjects().remove(project);
    }

    @Override
    public ProjectCodesDto getCodeList(CustomUserDetails customUserDetails,
                                             Long projectId) {
        User user = customUserDetails.getUser();
        ProjectCodesDto dto = new ProjectCodesDto();
        for(Project p : user.getProjects()){
            if(Objects.equals(p.getId(), projectId)){
                dto.setCodes(p.getCodes());
                break;
            }
        }


        return dto;
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new IllegalArgumentException("프로젝트 정보가 없습니다."));
//
//        return project.getCodes();
    }

}
