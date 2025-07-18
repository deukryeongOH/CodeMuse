package codemuse.project.domain.project.service;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.project.dto.CreateProjectDto;
import codemuse.project.domain.project.dto.ProjectCodesDto;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.global.security.spring.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ProjectService {
    List<Project> findAllProject(CustomUserDetails customUserDetails);
    void generateProject(CreateProjectDto dto, CustomUserDetails customUserDetails);
    void deleteProject(Long projectId, CustomUserDetails customUserDetails);
    ProjectCodesDto getCodeList(CustomUserDetails customUserDetails, Long projectId);

}
