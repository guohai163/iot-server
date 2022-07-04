package org.guohai.iot.protocol;

import io.netty.channel.Channel;
import lombok.Data;
import org.guohai.iot.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;




/**
 * @author guohai
 */
@Data
public class LoginProtocolHandler extends ProtocolBase{

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(LoginProtocolHandler.class);

    private String version;

    private String devId;

    private String sign;

    @Value("${netty.key}")
    private String key;

    public void onEvent(Channel channel){
        logger.info("LoginProtocolHandler进来的参数：devId = %s", devId);
        // 验签

        // 处理业务

        // 向客户端发送应答数据包
        AnswerProtocol answerProtocol = new AnswerProtocol();
        answerProtocol.setMsgType(EventType.CLIENT_REGISTER_ANSWER);
        answerProtocol.setTxNo(this.txNo);
        channel.writeAndFlush(answerProtocol);
    }

}
