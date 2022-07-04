package org.guohai.iot.protocol;

import lombok.Data;
import org.guohai.iot.event.EventType;

/**
 * 应答协议包
 */
@Data
public class AnswerProtocol {

    /**
     * 消息类型
     */
    protected EventType msgType;
    /**
     * 通讯号
     */
    protected long txNo;

    /**
     * 响应结果
     */
    protected String result;
}
