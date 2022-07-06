package org.guohai.iot.session;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 流量统计
 * 包含进出站的包和大小
 * @author guohai
 */
public class TrafficStatistics {

    static AtomicInteger inPack = new AtomicInteger(0);

    static AtomicInteger outPack = new AtomicInteger(0);

    static AtomicLong inByte = new AtomicLong(0);

    static AtomicLong outByte = new AtomicLong(0);

    /**
     * 增加进站数据包
     * @param byteSize 增加的数据包大小
     */
    public static void addInPack(long byteSize){
        inPack.getAndIncrement();
        inByte.getAndAdd(byteSize);
    }

    /**
     * 增加出站数据包
     * @param byteSize 增加的数据包大小
     */
    public static void addOutPack(long byteSize){
        outPack.getAndIncrement();
        outByte.getAndAdd(byteSize);
    }

    public static int getInPack(){
        return inPack.get();
    }

    public static int getOutPack(){
        return outPack.get();
    }

    public static long getInByte(){
        return inByte.get();
    }

    public static long getOutByte(){
        return outByte.get();
    }
}
