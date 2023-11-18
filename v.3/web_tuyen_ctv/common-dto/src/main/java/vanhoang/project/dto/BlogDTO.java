package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTO;

@Getter
@Setter
public class BlogDTO extends BaseDTO {

    private String blogKey;
    private String majorImageUrl;
    private String title;
    private String thumbnail;
    private String content;
    private Integer views;
    private Double rate;
}
