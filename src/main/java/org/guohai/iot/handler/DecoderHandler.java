package org.guohai.iot.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * 识别到一个正确的json数据，进行处理
     * @param ctx channel
     * @param buffer bytebuff
     * @param index 此次包的开始点
     * @param length 此次包的长度
     * @return
     */
    @Override
    protected ByteBuf extractObject(ChannelHandlerContext ctx, ByteBuf buffer,
                                    int index, int length){
        try{
            // 首先按指定的位置标记从 buffer中读取数据到新的bytebuf中。
            // 这里的ByteBuf是netty重写的nio中的ByteBuffer性能更好
            ByteBuf byteBuf = buffer.slice(index, length);

            try (ByteBufInputStream inputStream = new ByteBufInputStream(byteBuf)) {
                String message = byteBuf.readSlice(length).toString(0, length, CharsetUtil.UTF_8);
                logger.info(message);
                // 测试阶段直接回写数据
                ctx.writeAndFlush(byteBuf);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Unpooled.EMPTY_BUFFER;
    }
}
