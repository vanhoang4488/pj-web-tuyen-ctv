package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTO;

import java.util.Date;

@Getter
@Setter
public class UserDTO extends BaseDTO {

    private String fullName;
    private Date birthDay;
    private Integer gender;
    private String email;
}
