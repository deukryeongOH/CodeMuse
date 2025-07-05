package codemuse.project.domain.user.service.impl;

import codemuse.project.global.role.Role;
import codemuse.project.domain.user.dto.UserDetailsDto;
import codemuse.project.domain.user.dto.UserJoinDto;
import codemuse.project.domain.user.dto.UserResetPwdDto;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.repository.UserRepository;
import codemuse.project.domain.user.service.UserService;
import codemuse.project.global.security.spring.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User join(UserJoinDto dto) {
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

        return user;
    }

    @Override
    public User findIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일 가진 회원이 존재하지 않습니다."));
    }

    @Override
    public String recoverPassword(String accountId, String email) {
        User findUser = findUser(accountId);

        if(!findUser.getEmail().equals(email)){
            throw new IllegalArgumentException("해당 계정아이디를 가진 회원의 이메일과 일치하지 않습니다.");
        }

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$%^&*";
        String tempPwd = RandomStringUtils.random(10, 0, characters.length(), false, false, characters.toCharArray(), new SecureRandom());

        findUser.setPassword(passwordEncoder.encode(tempPwd));
        userRepository.save(findUser);

        return tempPwd;
    }

    @Override
    public void resetPassword(UserResetPwdDto dto) {
        User findUser = findUser(dto.getAccountId());

        if(!passwordEncoder.matches(dto.getTempPwd(), findUser.getPassword())){
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if(!dto.getResetPwd().equals(dto.getConfirmPwd())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        findUser.setPassword(passwordEncoder.encode(dto.getResetPwd()));
        userRepository.save(findUser);
    }

    @Override
    public void updateUserDetails(CustomUserDetails customUserDetails, UserDetailsDto dto) {
        User findUser = findUser(customUserDetails.getUsername());

        findUser.setName(dto.getName());
        findUser.setEmail(dto.getEmail());
        findUser.setNickName(dto.getNickName());

        userRepository.save(findUser);
    }

    @Override
    public UserDetailsDto getDetailsForm(CustomUserDetails customUserDetails) {
        User user = findUser(customUserDetails.getUsername());

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setAccountId(user.getAccountId());
        userDetailsDto.setName(user.getName());
        userDetailsDto.setEmail(user.getEmail());
        userDetailsDto.setNickName(user.getNickName());

        return userDetailsDto;
    }

    @Override
    public void changePassword(String accountId, String password, String change, String confirm) {
        User findUser = findUser(accountId);

        if(!passwordEncoder.matches(password, findUser.getPassword())){
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if(!change.equals(confirm)){
            throw new IllegalArgumentException("변경할 비밀번호가 일치하지 않습니다.");
        }

        findUser.setPassword(passwordEncoder.encode(change));
        userRepository.save(findUser);
    }

    private boolean validateUserAccountId(UserJoinDto dto){
        return userRepository.existsByAccountId(dto.getAccountId());
    }

    private User findUser(String accountId){
        return userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

    }

}
