package org.guohai.iot.session;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * 会话的基本数据
 */
@Data
public class SessionInfo {
    /**
     * 设备的channel，标识连接
     */
    private Channel channel;

    /**
     * 终端设备的唯一编号，供程序内部标识
     */
    private String devId;

    /**
     * 终端设备的版本
     */
    private String version;
}
