package codemuse.project.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPwdDto {
    private String accountId;
    private String tempPwd;
    private String resetPwd;
    private String confirmPwd;
}
