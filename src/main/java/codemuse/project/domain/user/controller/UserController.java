package codemuse.project.domain.user.controller;


import codemuse.project.domain.user.dto.UserDetailsDto;
import codemuse.project.domain.user.entity.User;
import codemuse.project.domain.user.service.UserService;
import codemuse.project.global.security.spring.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/myPage")
    public String myPageForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             Model model){

        UserDetailsDto dto = userService.getDetailsForm(customUserDetails);

        model.addAttribute("userDetailsDto", dto);
        return "user/myPage";
    }

    @PostMapping("/myPage")
    public String myPageForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @ModelAttribute("userDetailsDto") UserDetailsDto dto){

        userService.updateUserDetails(customUserDetails, dto);
        return "redirect:/dashboard";
    }

    /* 토큰 생성을 accountId로 하기 때문에 accountId는 변경되어선 X
    만약 변경 시 토큰이 깨져 인증 실패할 가능성이 있음
    @GetMapping("/changeAccountId")
    public String changeAccountIdForm(Model model){
        return "user/changeAccountId";
    }

    @PostMapping("/changeAccountId")
    public String changeAccountIdForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestParam("accountId") String accountId){

        userService.changeAccountId(customUserDetails, accountId);
        return "redirect:/user/myPage";
    }
*/
    @GetMapping("/changePassword")
    public String changePasswordForm(Model model){
        return "user/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePasswordForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     @RequestParam("password") String password,
                                     @RequestParam("changePassword") String change,
                                     @RequestParam("confirmPassword") String confirm){

        userService.changePassword(customUserDetails.getUsername(), password, change, confirm);
        return "redirect:/user/myPage";
    }

}
