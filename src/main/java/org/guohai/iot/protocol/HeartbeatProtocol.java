package org.guohai.iot.protocol;

import org.guohai.iot.event.EventType;

/**
 * 心跳包
 * @author guohai
 */
public class HeartbeatProtocol extends ProtocolBase{

    public HeartbeatProtocol(){
        this.msgType = EventType.HEART_BEAT;
        this.txNo = System.currentTimeMillis();
    }
}
