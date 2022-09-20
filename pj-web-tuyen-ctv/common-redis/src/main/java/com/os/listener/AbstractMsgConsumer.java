package com.os.listener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMsgConsumer {

    public void onError(String msg){
        log.error("====> error: {}", msg);
    }

    public abstract void onMessage(String msg);
}
