package org.guohai.iot.protocol;

import io.netty.channel.Channel;

/**
 * 协议处理的接口
 * @author guohai
 */
public interface ProtocolHandler {

    /**
     * 事件处理
     * @param channel socket channel
     */
    void onEvent(Channel channel);
}
