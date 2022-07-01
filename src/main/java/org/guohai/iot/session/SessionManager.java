package org.guohai.iot.session;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 会话管理
 * @author guohai
 */
@Component
public class SessionManager {

    /**
     * 当前服务器预估的最大连接数
     */
    private static final int SERVER_CONNECT_NUM = 5000;

    /**
     * 存储会话,为了防止使用map时进行动态扩容，初始化时直接指定一个预估的单服务器连接数
     */
    private final Map<Channel, SessionInfo> sessions = new HashMap<>(SERVER_CONNECT_NUM);
    /**
     * 存储管道
     */
    private final Map<String, Channel> channels = new HashMap<>(SERVER_CONNECT_NUM);

    /**
     * 增加会话，当终端连接上来就进行注册。
     * 终端发送login包时再更新会话属性
     * @param channel 通道
     */
    public void addSession(Channel channel) {
        SessionInfo session = new SessionInfo();
        session.setChannel(channel);
        sessions.put(channel, session);
    }

    /**
     * 终端登录后补充会话信息,现时增加channel
     * @param channel channel
     * @param devId 设备ID
     * @param version 设备版本
     */
    public void setSession(Channel channel, String devId, String version){
        SessionInfo session = sessions.get(channel);
        session.setVersion(version);
        session.setDevId(devId);

        channels.put(devId, channel);
    }

    /**
     * 通过channel获得会话
     * @param channel channel
     * @return 会话实体
     */
    public SessionInfo getSession(Channel channel) {
        return sessions.get(channel);
    }

    /**
     * 通过设备ID获取 channel
     * @param devId 设备标识
     * @return Channel
     */
    public Channel getChannel(String devId) {
        return channels.get(devId);
    }

    /**
     * 移除会话，当终端断开时请求
     * @param channel channel
     */
    public void removeSession(Channel channel) {

        SessionInfo sessionInfo = this.getSession(channel);
        if(sessionInfo != null) {
            if(sessionInfo.getDevId() != null && !sessionInfo.getDevId().isEmpty()) {
                // 如果设备已经登录过，还需要同时移除channel
                sessions.remove(channel);
                channels.remove(sessionInfo.getDevId());
            } else {
                sessions.remove(channel);
            }
        }
    }

    /**
     * 获取当前的会话数
     * @return 数量
     */
    public int getSessionCount(){
        return sessions.size();
    }

    /**
     * 获得chanel的数量
     * 注册成功的
     * @return 数量
     */
    public int getChannelCount(){
        return channels.size();
    }
}
