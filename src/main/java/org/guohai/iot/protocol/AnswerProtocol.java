package org.guohai.iot.protocol;

import lombok.Data;
import org.guohai.iot.event.EventType;

/**
 * 应答协议包
 * @author guohai
 */
@Data
public class AnswerProtocol extends ProtocolBase {

    /**
     * 响应结果
     */
    protected String result;
}
