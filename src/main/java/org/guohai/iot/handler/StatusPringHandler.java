package org.guohai.iot.handler;

import org.guohai.iot.session.SessionManager;
import org.guohai.iot.session.TrafficStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务程序状态信息打印
 *
 * @author guohai
 */
@Component
public class StatusPringHandler implements Runnable {

    /**
     * 会话管理
     */
    @Autowired
    SessionManager sessionManager;
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(StatusPringHandler.class);


    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        logger.info(
                "\n"+
                "+---------+---------+------------+-----------+-------------+---------------+-------------+---------------+\n" +
                "| session | channel | main queue | log queue |   in pack   |    in byte    |   out pack  |    out byte   |\n" +
                "+---------+---------+------------+-----------+-------------+---------------+-------------+---------------+\n"+
                "| "+String.format("%7d",sessionManager.getSessionCount())+" | "+String.format("%7d",sessionManager.getChannelCount())+" |    "+String.format("%7d",0)+" |   "+String.format("%7d",0)+" |     " +
                        String.format("%7d", TrafficStatistics.getInPack())+" |     "+String.format("%9d", TrafficStatistics.getInByte())+" |     "+String.format("%7d", TrafficStatistics.getOutPack())+" |     "+String.format("%9d", TrafficStatistics.getOutByte())+" |\n"+
                "+---------+---------+------------+-----------+-------------+---------------+-------------+---------------+\n");
    }
}
