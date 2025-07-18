package codemuse.project.domain.user.service.impl;

import codemuse.project.domain.user.dto.UserJoinDto;
import codemuse.project.domain.user.dto.UserLoginDto;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceImplTest {

    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void sign(){
        UserJoinDto dto = new UserJoinDto();
        dto.setAccountId("test123");
        dto.setName("test");
        dto.setEmail("test@example.com");
        dto.setNickName("tt");
        dto.setPassword("testpwd");

        User savedUser = userService.join(dto);
        String accountId = "test123";
        String confirm = "testpwd";

        assertEquals(accountId, savedUser.getAccountId());
        assertTrue(passwordEncoder.matches(confirm, savedUser.getPassword()));
    }

    @Test
    void login(){
        UserJoinDto dto = new UserJoinDto();
        dto.setAccountId("test123");
        dto.setName("test");
        dto.setEmail("test@example.com");
        dto.setNickName("tt");
        dto.setPassword("testpwd");

        User savedUser = userService.join(dto);

        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setAccountId("test123");
        loginDto.setPassword("testpwd");

        assertEquals(dto.getAccountId(), loginDto.getAccountId());
        assertTrue(passwordEncoder.matches(loginDto.getPassword(), savedUser.getPassword()));
    }
}