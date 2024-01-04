package vanhoang.project.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vanhoang.project.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "tags")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class TagEntity extends BaseEntity{

    /**
     * keyword của từng tag = id nhưng hiển thị ngắn gọn hơn để thuận tiện cho việc xem nhiều tag
     * Tao muốn sửa lại: để keyTag là id luôn, như thế sẽ tiết kiệm query hơn vì việc tra cứu thông tin
     * blog sẽ diễn ra nhanh hơn rất nhiều. Nên nếu như việc tìm kiếm thông tin 1 bài viết mất đến 3 query thì
     * nếu lấy ra 100 bài viết ta cần 201 query (1 query lấy 100 blog, 100 query lấy tag của blog trong blogs_tags,
     * 100 query lấy tagKey tương ứng - nếu sử dụng lớp convertor để chuyển đổi giữa BlogEntity -> BlogDTO)
     * ==> việc làm này có thể tích kiệm đến 100 query để lấy tagKey.
     *
     * ==> nếu xác định tagKey là khóa chính thì có nghĩa bảng tags chỉ nên thêm vào chứ không khuyến khích
     * xóa đi hay cập nhật.
     */
    @Id
    @NotEmpty(message = "tag.key.empty")
    @Size(max = 8, message = "tag.key.size")
    private String tagKey;

    /**
     * thông tin chi tiết của tag
     */
    @Column
    @NotEmpty(message = "tag.detail.empty")
    @Size(max = 255, message = "tag.detail.size")
    private String detail;

    /** ràng buộc: n to n: Blog -> Blogs_Tags*/
    @OneToMany(fetch =  FetchType.LAZY, cascade = CascadeType.REMOVE,
                mappedBy = "tag")
    private List<BlogTagEntity> blogs;

    /** ràng buộc: 1 to n: TagClass*/
    @JoinColumn(name = "classId")
    @ManyToOne(fetch = FetchType.LAZY)
    private TagClassEntity clazz;
}
