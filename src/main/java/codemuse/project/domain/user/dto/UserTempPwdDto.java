package codemuse.project.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTempPwdDto {
    private String accountId;
    private String email;
}
