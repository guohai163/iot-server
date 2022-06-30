package org.guohai.iot.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;
import org.guohai.iot.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 解码器，用来接收从socket来的数据流
 * 这里我们继承下 JsonObjectDecoder 类，可以帮我们做json的自动解码
 * 解决socket中常见的 粘包 半包问题
 * @author guohai
 */
@Component
public class DecoderHandler extends JsonObjectDecoder {

    private static final Logger logger = LoggerFactory.getLogger(DecoderHandler.class);

    /**
     * 会话管理
     */
    @Autowired
    SessionManager sessionManager;

    /**
     * 当有客户端注册时调用
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ChannelConfig config = ctx.channel().config();

        DefaultSocketChannelConfig socketConfig = (DefaultSocketChannelConfig)config;
        // 此处三个参数决定 延迟情况
        // 连接时间 、往返延迟、 带宽。
        // 这三个参数设置的是权重
        // 因为我的连接会保持住 长连接不会频繁断开，所以 把连接时间权限设置的最低为0
        // 因为我们对往返延迟有一些容忍度，所以 第二参数是1
        // 对于带宽我们会有更大的需求，第三个参数设置为2 这就是目前的权重比
        // 延迟和带宽的性能是互斥的 , 延迟低 , 就意味着很小的包就要发送一次 , 其带宽就低了 , 延迟高了 , 每次积累很多数据才发送 , 其带宽就相应的提高了
        socketConfig.setPerformancePreferences(0,1,2);
        // NioSocketChannel在工作过程中，使用PooledByteBufAllocator来分配内存
        socketConfig.setAllocator(PooledByteBufAllocator.DEFAULT);

        super.channelRegistered(ctx);

        // 增加会话
        sessionManager.addSession(ctx.channel());
    }

    /**
     * 识别到一个正确的json数据，进行处理
     * @param ctx channel
     * @param buffer bytebuff
     * @param index 此次包的开始点
     * @param length 此次包的长度
     * @return 返回一个bytebuf做后续处理，如果不需要可以返回Unpooled.EMPTY_BUFFER
     */
    @Override
    protected ByteBuf extractObject(ChannelHandlerContext ctx, ByteBuf buffer,
                                    int index, int length){
        try{
            // 首先按指定的位置标记从 buffer中读取数据到新的bytebuf中。
            // 这里的ByteBuf是netty重写的nio中的ByteBuffer性能更好
            ByteBuf byteBuf = buffer.slice(index, length);

            // 把接收到的流转写成string字符串
            try (ByteBufInputStream inputStream = new ByteBufInputStream(byteBuf)) {

                String message = byteBuf.readSlice(length).toString(0, length, CharsetUtil.UTF_8);
                logger.info(message);
                // 测试阶段直接回写数据
                ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Unpooled.EMPTY_BUFFER;
    }
}
