package com.cc.llogger.writer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CommitStatistics: ${description}
 *
 * @author chenhao
 * @version 1.0
 * @date 2021-3-5 17:17
 */
public class CommitStatistics {
    private static long totalTime = 0;
    private static long totalLogs = 0;
    private static long commitTimes = 0;
    private static volatile AtomicInteger tempLogs = new AtomicInteger();

    private CommitStatistics(){};

    public static void commitIndex(long times){
        totalTime += times;
        totalLogs += tempLogs.getAndSet(0);
        commitTimes++;
    }

    public static void addLogs(){
        tempLogs.incrementAndGet();
    }

    public static long getTotalTime(){
        return totalTime;
    }

    public static long getTotalLogs(){
        return totalLogs;
    }

    public static long getTotalCommit(){
        return commitTimes;
    }
}
