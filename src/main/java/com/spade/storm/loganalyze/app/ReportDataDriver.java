package com.spade.storm.loganalyze.app;

import com.spade.storm.loganalyze.app.runner.OneMinuteRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Brian
 * @Date: 2020/06/02/21:32
 * @Description:
 * 定时计算每个指标在不同维度的增量数据
 */
public class ReportDataDriver {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        //计算每分钟的增量数据
        executor.scheduleAtFixedRate(new OneMinuteRunner(), 0, 60, TimeUnit.SECONDS);
        
    }
}
