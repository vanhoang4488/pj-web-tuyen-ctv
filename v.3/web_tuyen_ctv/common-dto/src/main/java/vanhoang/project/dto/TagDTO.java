package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTO;

@Getter
@Setter
public class TagDTO extends BaseDTO {

    private String tagKey;
    private String detail;
}
