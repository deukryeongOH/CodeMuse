package codemuse.project.domain.user.service.impl;

import codemuse.project.domain.role.Role;
import codemuse.project.domain.user.dto.UserJoinDto;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.repository.UserRepository;
import codemuse.project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void join(UserJoinDto dto) {
        if(validateUserAccountId(dto)){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .name(dto.getName())
                .accountId(dto.getAccountId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .nickName(dto.getNickName())
                .createdAt(LocalDateTime.now())
                .build();

        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

    }

    public boolean validateUserAccountId(UserJoinDto dto){
        return userRepository.existsByAccountId(dto.getAccountId());

    }
}
