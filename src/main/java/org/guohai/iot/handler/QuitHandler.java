package org.guohai.iot.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 主要 处理退出业务,线程安全的可以进行贡献
 * @author guohai
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {



    /**
     * 会话退出时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("会话退出", ctx.channel());
        cleanSession(ctx.channel());
    }

    /**
     * 解码失败时，异常客户端踢掉
     * @param ctx ChannelHandlerContext
     * @param cause Throwable
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();

            log.error("解码异常捕获:[{}:{}]",remoteAddress.getAddress().getHostAddress(),remoteAddress.getPort(), cause);
        } finally {
            ctx.channel().close();
        }
    }

    /**
     * 清空会话
     * @param channel
     */
    private void cleanSession(Channel channel){

    }
}
