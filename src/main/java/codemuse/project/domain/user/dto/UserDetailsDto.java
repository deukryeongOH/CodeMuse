package codemuse.project.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDto {

    private String accountId;
    private String name;
    private String email;
    private String nickName;

}
