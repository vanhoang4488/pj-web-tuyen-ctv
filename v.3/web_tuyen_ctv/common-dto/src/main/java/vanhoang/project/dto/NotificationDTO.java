package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTOId;

@Setter
@Getter
public class NotificationDTO extends BaseDTOId {

    private String title;
    private String content;
    private Integer notRead;
}
