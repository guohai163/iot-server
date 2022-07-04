package org.guohai.iot.event;

import com.google.gson.Gson;
import com.lmax.disruptor.EventHandler;
import io.netty.channel.Channel;
import org.guohai.iot.protocol.LoginProtocolHandler;
import org.springframework.stereotype.Component;

/**
 * 主事件的消费者
 * @author guohai
 */
@Component
public class MainEventHandler implements EventHandler<EventInfo> {
    /**
     * 当有事件时
     * @param eventInfo
     * @param l
     * @param b
     * @throws Exception
     */
    @Override
    public void onEvent(EventInfo eventInfo, long l, boolean b) throws Exception {
        EventType eventType = eventInfo.getEventType();
        if(eventType == EventType.CLIENT_REGISTER){
            // 客户端注册
            LoginProtocolHandler loginProtocolHandler = new Gson().fromJson(eventInfo.getMessage(), LoginProtocolHandler.class);
            loginProtocolHandler.onEvent(eventInfo.getChannel());
        }
    }
}
