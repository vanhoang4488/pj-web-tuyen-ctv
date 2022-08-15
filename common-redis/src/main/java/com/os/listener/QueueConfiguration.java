package com.os.listener;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class QueueConfiguration {

    private final QueueName queueName;

    private final AbstractMsgConsumer consumer;

}
