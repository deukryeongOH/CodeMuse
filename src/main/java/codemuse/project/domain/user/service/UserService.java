package codemuse.project.domain.user.service;

import codemuse.project.domain.user.dto.UserJoinDto;
import codemuse.project.domain.user.dto.UserResetPwdDto;
import codemuse.project.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User join(UserJoinDto dto);
    User findIdByEmail(String email);
    String recoverPassword(String accountId, String email);
    void resetPassword(UserResetPwdDto dto);
}
