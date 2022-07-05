package org.guohai.iot.event;

import io.netty.channel.Channel;
import org.guohai.iot.protocol.AnswerProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 登录事件
 * @author guohai
 */
@Component
public class LoginEventHandler implements IotEventHandler {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(LoginEventHandler.class);

    /**
     * 密钥
     */
    @Value("${netty.key}")
    private String key;

    /**
     * 事件处理
     *
     * @param channel socket channel
     */
    @Override
    public void onEvent(Channel channel) {
        logger.info("这是一个设备的登陆包");
        // 验签

        // 处理业务

        // 向客户端发送应答数据包
        AnswerProtocol answerProtocol = new AnswerProtocol();
        answerProtocol.setMsgType(EventType.CLIENT_REGISTER_ANSWER);
        channel.writeAndFlush(answerProtocol);
    }
}
