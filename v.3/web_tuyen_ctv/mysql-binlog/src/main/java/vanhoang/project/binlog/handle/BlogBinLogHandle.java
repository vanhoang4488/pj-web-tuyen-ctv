package vanhoang.project.binlog.handle;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vanhoang.project.annotation.BinlogEntityListener;
import vanhoang.project.binlog.bean.BinlogItem;
import vanhoang.project.binlog.handle.base.BinLogHandle;
import vanhoang.project.entity.BlogEntity;
import vanhoang.project.entity.statistic.BlogKeyEntity;
import vanhoang.project.repository.statistic.BlogKeyRepository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@BinlogEntityListener(listen = BlogEntity.class)
public class BlogBinLogHandle implements BinLogHandle {
    private static final String BLOG_KEY = "blogKey";
    private final BlogKeyRepository blogKeyRepository;

    @Override
    public void handle(BinlogItem binLogItem) {
        log.info("====> update frequency field of blog_keys when insert blog on blogs table: {}", binLogItem);
        List<Map<String, Serializable>> afterRows = binLogItem.getAfterRows();
        if (EventType.isWrite(binLogItem.getEventType()) &&
                afterRows != null && !afterRows.isEmpty()) {
            // lấy số lượng blogKey và số lần cần thêm frequency của từng blogKey.
            Map<String, Short> blogKeyFrequencyMap = new HashMap<>();
            for (Map<String, Serializable> row : afterRows) {
                Serializable blogKeySerializable = row.get(BLOG_KEY);
                String blogKey = ((String) blogKeySerializable).replaceAll("\\d+", "");
                if (!blogKeyFrequencyMap.containsKey(blogKey)) {
                    blogKeyFrequencyMap.put(blogKey, (short) 1);
                }
                else {
                    short frequency = blogKeyFrequencyMap.get(blogKey);
                    blogKeyFrequencyMap.put(blogKey, ++frequency);
                }
            }

            for (Map.Entry<String, Short> entry : blogKeyFrequencyMap.entrySet()) {
                Optional<BlogKeyEntity> optionalBlogKeyEntity = blogKeyRepository.findBlogKeyByBlogKey(entry.getKey());
                if (optionalBlogKeyEntity.isPresent()) {
                    BlogKeyEntity blogKeyEntity = optionalBlogKeyEntity.get();
                    short frequency = (short) (blogKeyEntity.getFrequency() + entry.getValue());
                    blogKeyEntity.setFrequency(frequency);
                    blogKeyRepository.merge(blogKeyEntity);
                    log.info("====> update blog_keys table with key: {}, frequency: {}", entry.getKey(), blogKeyEntity.getFrequency() + entry.getValue());
                }
                else {
                    BlogKeyEntity blogKeyEntity = new BlogKeyEntity();
                    blogKeyEntity.setBlogKey(entry.getKey());
                    blogKeyEntity.setFrequency(entry.getValue());
                    blogKeyRepository.persist(blogKeyEntity);
                    log.info("====> add blog_keys table with key: {}, frequency: {}", entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
