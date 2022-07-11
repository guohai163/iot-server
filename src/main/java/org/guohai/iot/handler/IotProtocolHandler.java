package org.guohai.iot.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.guohai.iot.event.MainEventProducer;
import org.guohai.iot.session.TrafficStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 协议包解析
 * @author guohai
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class IotProtocolHandler extends MessageToMessageDecoder<ByteBuf> {


    /**
     * 主事件循环
     */
    private final MainEventProducer mainEventProducer;

    public IotProtocolHandler(MainEventProducer mainEventProducer) {
        this.mainEventProducer = mainEventProducer;
    }

    /**
     * Decode from one message to an other. This method will be called for each written message that can be handled
     * by this decoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageDecoder} belongs to
     * @param msg the message to decode to an other one
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        try{
            // 计数器
            TrafficStatistics.addInPack(msg.readableBytes());
            // 把接收到的流转写成string字符串
            String message = msg.toString(CharsetUtil.UTF_8);
            log.info(message);

            // 向队列发布服务
            mainEventProducer.onData(ctx.channel(), message);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
