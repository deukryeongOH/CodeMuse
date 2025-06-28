package codemuse.project.domain.user.service;

import codemuse.project.domain.user.dto.UserJoinDto;
import codemuse.project.domain.user.dto.UserLoginDto;
import codemuse.project.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void join(UserJoinDto dto);
    User findIdByEmail(String email);
}
