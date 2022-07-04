package org.guohai.iot.event;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author guohai
 */
@Data
public class EventInfo {
    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     *渠道
     */
    private Channel channel;

    /**
     * 协议原文
     */
    private String message;
}
