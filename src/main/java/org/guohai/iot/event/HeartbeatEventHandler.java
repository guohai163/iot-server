package org.guohai.iot.event;

import io.netty.channel.Channel;
import org.guohai.iot.handler.StatusPringHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 心跳事件处理
 * @author guohai
 */
@Component
public class HeartbeatEventHandler implements IotEventHandler{

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatEventHandler.class);


    /**
     * 事件处理
     *
     * @param channel socket channel
     */
    @Override
    public void onEvent(Channel channel) {
        logger.info("这是一个心跳包");
    }
}
