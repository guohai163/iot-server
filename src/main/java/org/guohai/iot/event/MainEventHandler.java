package org.guohai.iot.event;

import com.google.gson.Gson;
import com.lmax.disruptor.EventHandler;
import org.guohai.iot.protocol.LoginProtocol;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 主事件的消费者
 * @author guohai
 */
@Component
public class MainEventHandler implements EventHandler<EventInfo> {

    /**
     * 事件MAP
     */
    private final Map<EventType, IotEventHandler> eventMap = new HashMap<>(2);


    public MainEventHandler(LoginEventHandler loginEventHandler, HeartbeatEventHandler heartbeatEventHandler){
        eventMap.put(EventType.CLIENT_REGISTER, loginEventHandler);
        eventMap.put(EventType.HEART_BEAT, heartbeatEventHandler);
    }


    /**
     * 当有事件时
     * @param eventInfo
     * @param l
     * @param b
     * @throws Exception
     */
    @Override
    public void onEvent(EventInfo eventInfo, long l, boolean b) throws Exception {

        IotEventHandler eventHandler = eventMap.get(eventInfo.getEventType());

        eventHandler.onEvent(eventInfo.getChannel());
    }
}
