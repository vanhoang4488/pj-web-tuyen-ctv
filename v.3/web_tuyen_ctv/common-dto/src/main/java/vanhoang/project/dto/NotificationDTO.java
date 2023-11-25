package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTO;

@Setter
@Getter
public class NotificationDTO extends BaseDTO {

    private String title;
    private String content;
    private Integer notRead;
}
