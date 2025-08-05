package codemuse.project.domain.project.service;

import codemuse.project.domain.code.entity.Code;
import codemuse.project.domain.project.dto.CodeDto;
import codemuse.project.domain.project.dto.CreateProjectDto;
import codemuse.project.domain.project.dto.ProjectDto;
import codemuse.project.domain.project.entity.Project;
import codemuse.project.global.security.spring.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ProjectService {
    List<ProjectDto> findAllProject(CustomUserDetails customUserDetails);
    void generateProject(CreateProjectDto dto, CustomUserDetails customUserDetails);
    void deleteProject(Long projectId, CustomUserDetails customUserDetails);
    List<CodeDto> getCodeList(CustomUserDetails customUserDetails, Long projectId);
    ProjectDto getProject(Long projectId);
    void deleteCodeFromProject(Long projectId, Long codeId);
    String EncodeLink(List<CodeDto> codes) throws JsonProcessingException;
}
