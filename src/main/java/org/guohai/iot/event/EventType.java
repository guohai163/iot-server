package org.guohai.iot.event;

import com.google.gson.annotations.SerializedName;

/**
 * 事件类型
 * @author guohai
 */

public enum EventType {
    /**
     * 客户端连接
     */
    CLIENT_CONNECT,
    /**
     * 客户端注册
     */
    @SerializedName("10")
    CLIENT_REGISTER,

    @SerializedName("11")
    CLIENT_REGISTER_ANSWER,

    /**
     * 心跳检查
     */
    @SerializedName("20")
    HEART_BEAT,

    /**
     * 心跳检查
     */
    @SerializedName("21")
    HEART_BEAT_ANSWER,
    /**
     * 客户端发过来的协议包
     */
    CLIENT_PROTO_COMMING,

    /**
     * 客户端关闭连接
     */
    CLIENT_DISCONNECT,

    /**
     * 下发命令
     */
    SEND_COMMAND
}
