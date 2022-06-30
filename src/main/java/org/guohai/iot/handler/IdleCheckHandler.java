package org.guohai.iot.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空闲检测器
 * @author guohai
 */
public class IdleCheckHandler extends ChannelDuplexHandler {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StatusPringHandler.class);

    /**
     * 空闲会话检测
     * @param ctx 管道
     * @param evt 事件对象
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                // TODO: 读空闲，准备断开客户端
                logger.debug("读空闲，准备断开客户端");
            } else if (e.state() == IdleState.WRITER_IDLE) {
                logger.debug("写空闲，下行一条心跳保持连接");
                // TODO: 下行数据先写死
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("{\"msgType\": 20, \"txNo\": \"1234567890123\"}\n", CharsetUtil.UTF_8));
            }
        }
    }
}
