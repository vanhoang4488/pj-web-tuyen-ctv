package vanhoang.project.convertor;

import org.mapstruct.Mapper;
import vanhoang.project.dto.NotificationDTO;
import vanhoang.project.entity.NotificationEntity;

@Mapper
public interface NotificationConvertor {

    NotificationDTO convert(NotificationEntity entity);
}
