package org.guohai.iot.protocol;

import io.netty.channel.Channel;

/**
 * 心跳包
 * @author guohai
 */
public class HeartbeatProtocolHandler extends ProtocolBase implements ProtocolHandler{


    /**
     * 事件处理
     *
     * @param channel socket channel
     */
    @Override
    public void onEvent(Channel channel) {

    }
}
