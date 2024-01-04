package vanhoang.project.convertor;

import javax.annotation.processing.Generated;
import vanhoang.project.dto.NotificationDTO;
import vanhoang.project.entity.NotificationEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-03T01:21:18+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.14 (Oracle Corporation)"
)
public class NotificationConvertorImpl implements NotificationConvertor {

    @Override
    public NotificationDTO convert(NotificationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setCreateTime( entity.getCreateTime() );
        notificationDTO.setUpdateTime( entity.getUpdateTime() );
        notificationDTO.setId( entity.getId() );
        notificationDTO.setTitle( entity.getTitle() );
        notificationDTO.setContent( entity.getContent() );

        return notificationDTO;
    }
}
