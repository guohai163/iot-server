package org.guohai.iot.event;

import com.google.gson.Gson;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.guohai.iot.protocol.ProtocolBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;

/**
 * 主事件队列生产者
 * @author guohai
 */
@Component
public class MainEventProducer {

    private final Disruptor<EventInfo> disruptor;
    /**
     * 存储数据一个环形队列
     */
    private final RingBuffer<EventInfo> ringBuffer;

    /**
     * 主事件消费者
     */
    MainEventHandler mainEventHandler;

    /**
     * 初始化的队列大小，生产环境中尽量设置的大一些
     */
    private final int INIT_LOGIC_EVENT_CAPACITY = 1024 ;

    public MainEventProducer(MainEventHandler mainEventHandler){
        this.mainEventHandler = mainEventHandler;
        // 初始化
        disruptor = new Disruptor<>(EventInfo::new, INIT_LOGIC_EVENT_CAPACITY,
                DaemonThreadFactory.INSTANCE);
        // 指定消费者
        disruptor.handleEventsWith(mainEventHandler);
        ringBuffer = disruptor.getRingBuffer();
        //启动队列
        disruptor.start();
    }

    /**
     * 发布一条消息入队
     * @param channel
     * @param message
     */
    public void onData(Channel channel, String message){
        // 获取队列里的位置 ，准备入队、
        long sequence = ringBuffer.next();

        ProtocolBase protocolBase = new Gson().fromJson(message, ProtocolBase.class);
        try{
            EventInfo newEventInfo = ringBuffer.get(sequence);
            newEventInfo.setEventType(protocolBase.getMsgType());
            newEventInfo.setChannel(channel);
            newEventInfo.setMessage(message);
        }finally {
            ringBuffer.publish(sequence);
        }
    }

    /**
     * 停止服务
     */
    public void stop(){
        disruptor.shutdown();
    }
}
