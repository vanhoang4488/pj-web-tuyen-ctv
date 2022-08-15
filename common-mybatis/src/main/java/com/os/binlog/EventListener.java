package com.os.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;

public class EventListener implements BinaryLogClient.EventListener {

    private final ThreadPoolTaskExecutor executor;

    public EventListener(int nThread, int queueSize){
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(nThread);
        executor.setMaxPoolSize(nThread);
        executor.setQueueCapacity(queueSize);
        executor.setThreadNamePrefix("binlog-thread-");
    }

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();

        // todo làm gì đó!
        if(eventType == EventType.TABLE_MAP){

        }

        //những hành động thêm, xóa, sửa
        //-> lấy thông tin chi tiết -> thao tác -> lưu lại vào database.
        if(EventType.isWrite(eventType)){

        }
        else if(EventType.isUpdate(eventType)){

        }
        else if(EventType.isDelete(eventType)){

        }
    }

    private void executeEvent(Event event){
        executor.submit(new Callable<Event>() {

            @Override
            public Event call() throws Exception {
                return null;
            }
        });
    }
}
