package org.guohai.iot.handler;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import org.guohai.iot.protocol.AnswerProtocol;

/**
 * 出站编码器
 * @author guohai
 */
public class EncoderHandler extends ChannelOutboundHandlerAdapter {

    /**
     *
     * @param ctx               the {@link ChannelHandlerContext} for which the write operation is made
     * @param msg               the message to write
     * @param promise           the {@link ChannelPromise} to notify once the operation completes
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg == null) {
            return;
        }

        if(msg instanceof AnswerProtocol) {
            AnswerProtocol message = (AnswerProtocol)msg;

            String json = new Gson().toJson(message);
            ByteBuf byteBuf = ctx.alloc().directBuffer(json.length(), json.length());
            System.out.println(byteBuf);
            byte[] bytes = json.getBytes(CharsetUtil.UTF_8);
            byteBuf.writeBytes(bytes);

            super.write(ctx,byteBuf,promise);
        }


        super.write(ctx,msg,promise);
    }
}