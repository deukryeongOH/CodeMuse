package codemuse.project.domain.user.controller;

import codemuse.project.domain.code.dto.CodeFeedBackDto;
import codemuse.project.domain.code.dto.UploadRequestDto;
import codemuse.project.domain.user.dto.UserJoinDto;
import codemuse.project.domain.user.dto.UserLoginDto;
import codemuse.project.domain.user.dto.UserResetPwdDto;
import codemuse.project.domain.user.dto.UserTempPwdDto;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.service.UserService;
import codemuse.project.global.ai.ChatGPTService;
import codemuse.project.global.security.cookie.CookieUtil;
import codemuse.project.global.security.jwt.TokenProvider;
import codemuse.project.global.security.spring.CustomUserDetails;
import codemuse.project.global.security.spring.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserHomeController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final UserService userService;
    private final ChatGPTService chatGPTService;


    @GetMapping("/signUp")
    public String signUpForm(Model model){
        model.addAttribute("userJoinDto", new UserJoinDto());
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@Valid @ModelAttribute("userJoinDto") UserJoinDto dto){

        userService.join(dto);
        return "redirect:/login";
    }

    @GetMapping({"/", "/login"})
    public String loginPage(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String Login(@ModelAttribute("userLoginDto") UserLoginDto dto,
            HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getAccountId(), dto.getPassword())
        );

        String accessToken = tokenProvider.createAccessToken(authentication);
        cookieUtil.create(response, accessToken);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        User user = userDetails.getUser();
        model.addAttribute("uploadRequestDto", new UploadRequestDto());
        model.addAttribute("name", user.getName());
        model.addAttribute("nickName", user.getNickName());
        model.addAttribute("projects", user.getProjects());
        return "dashboard";
    }

    @GetMapping("/findId")
    public String findId(Model model){
        String email = "";
        model.addAttribute("email", email);
        return "findId";
    }

    @PostMapping("/findId")
    public String findId(@RequestParam("email") String email, Model model){
        User user = userService.findIdByEmail(email);
        model.addAttribute("accountId", user.getAccountId());
        return "getId";
    }

    @GetMapping("/getId")
    public String getId(){
        return "getId";
    }


    @GetMapping("/findPassword")
    public String findPwdForm(Model model){
        model.addAttribute("userTempPwdDto", new UserTempPwdDto());
        return "findPassword";
    }

    // 추후에 이메일을 통한 비밀번호 알아내기로 고도화
    @PostMapping("/findPassword")
    public String findPwdForm(@RequestParam("accountId") String accountId,
                              @RequestParam("email") String email,
                              Model model){
        String tempPwd = userService.recoverPassword(accountId, email);
        model.addAttribute("tempPwd", tempPwd);
        return "tempPassword";
    }

    @GetMapping("/resetPassword")
    public String resetPwdForm(Model model){
        model.addAttribute("userResetPwdDto", new UserResetPwdDto());
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPwdForm(@ModelAttribute("UserResetPwdDto") UserResetPwdDto dto) {

        userService.resetPassword(dto);

        return "redirect:/login";

    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        cookieUtil.clear(response);
        return "redirect:/login?logout";
    }
}
