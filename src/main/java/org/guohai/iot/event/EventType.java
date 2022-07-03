package org.guohai.iot.event;

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
    CLIENT_REGISTER,

    /**
     * 心跳检查
     */
    HEART_BEAT,

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
