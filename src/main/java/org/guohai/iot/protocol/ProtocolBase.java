package org.guohai.iot.protocol;

import io.netty.channel.Channel;
import lombok.Data;
import org.guohai.iot.event.EventType;

/**
 * @author guohai
 */
@Data
public class ProtocolBase {

    /**
     * 消息类型
     */
    protected EventType msgType;

    /**
     * 通讯号
     */
    protected long txNo;
}
