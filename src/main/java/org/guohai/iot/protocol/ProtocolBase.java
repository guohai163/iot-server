package org.guohai.iot.protocol;

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
     * 设置ID
     */
    protected String devId;

    /**
     * 通讯号
     */
    protected long txnNo;
}
