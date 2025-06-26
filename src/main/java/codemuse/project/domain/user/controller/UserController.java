package codemuse.project.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @PostMapping("/manageProject")
    public String manageProject(){
        return "manageProject";
    }

}
