package vanhoang.project.dto;

import lombok.Getter;
import lombok.Setter;
import vanhoang.project.dto.base.BaseDTOId;

import java.util.List;

@Getter
@Setter
public class BlogDTO extends BaseDTOId {

    private String blogKey;
    private String majorImgUrl;
    private String title;
    private String thumbnail;
    private String content;
    private Integer views;
    private Double rate;
    /**
     * lúc hiển thị ra màn hình, người dùng cần biết 'key' của tag thôi
     * phía FE thì cần 2 thông tin: id, key và priority (tag_classes) vì 1 bài viết cần có loại tag này để tạo blogKey
     * ==> bỏ priority đi vì blogKey (lý do: đọc kỹ lại code tạo blogKey, nó chỉ yêu cầu có tag là được)
     * ==> Chốt FE cần 2 thông tin: id và key
     * Hỏi: Liệu id của Tag có cần thiết không. creat_time và update_time thì chắc chắn cần
     * Trả lời: theo tao là có cần id vì thực tế, 'key' của tag vẫn có thể thay đổi chỉ là không thể trùng nhau thôi.
     * ==> lúc lấy Blog từ database: BlogTagEntity cần lấy thêm TagEntity tương ứng.
     *
     * ==> bảng tags đã sửa lại, lấy tagKey làm id --> phía FE chỉ cần tagKey thôi là đủ.
     */
    private List<String> tags;
}
