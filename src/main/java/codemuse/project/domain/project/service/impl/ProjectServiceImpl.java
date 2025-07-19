package codemuse.project.domain.project.service.impl;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.code.repository.CodeRepository;
import codemuse.project.domain.project.dto.CreateProjectDto;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.domain.project.repository.ProjectRepository;
import codemuse.project.domain.project.service.ProjectService;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.repository.UserRepository;
import codemuse.project.global.security.spring.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;

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
    public List<Code> getCodeList(CustomUserDetails customUserDetails,
                                             Long projectId) {
        User user = customUserDetails.getUser();
        List<Code> codes = null;
        for(Project p : user.getProjects()){
            if(Objects.equals(p.getId(), projectId)){
                codes = new ArrayList<>(p.getCodes());
                break;
            }
        }

        if(codes == null){
            throw new IllegalArgumentException("해당 프로젝트에 저장된 코드가 없습니다.");
        }

        return codes;

//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new IllegalArgumentException("프로젝트 정보가 없습니다."));
//
//        return project.getCodes();
    }

    @Override
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트가 없습니다."));
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

}
